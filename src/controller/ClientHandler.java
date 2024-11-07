/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Click;
import model.ClickDao;
import model.Game;
import model.GameDao;
import model.Point;
import model.PointDao;
import model.User;
import model.UserDao;

public class ClientHandler implements Runnable{
    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private static List<GroupHandler> groupHandlers = new ArrayList<>();
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private volatile boolean running = true;
    private ClientHandler clientParner;
    private User user;
    private Game game;
    private List<Point> points = new ArrayList();
    private List<Point> clickedPoints = new ArrayList();
    private boolean  isReady = false;
    private int isContinuing = -1;
    private boolean setResult = false;
    
    public ClientHandler (Socket socket) {
        try {
            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.clientParner = null;
            this.user = (User) objectInputStream.readObject();
            
            requestAddUser();
            clientHandlers.add(this);
            
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    public String getClientUsername() {
        return user.getUsername();
    }
    
    public synchronized void sendMessage(Message message) {
        try {
            while(true) {
                if(socket.isConnected()) {
                    objectOutputStream.writeObject(message);
                    break;
                }
            }
            
        } catch(IOException e) {
            closeEverything();
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(running) {
                if(!socket.isConnected()) break;
                
                try {
                    Message message = (Message) objectInputStream.readObject();
                    switch (message.getCode()) {
                        // Message from Home screen
                        case "0" -> closeEverything();
                        case "1" -> handleGetAllUserRequest(message);
                        case "2" -> handleGetAllOnlineUserRequest(message);
                        case "4" -> transferRemoveUserRequest();
                        case "5" -> transferInviteRequest(message);
                        case "7" -> transferAgreementResponse(message);
                        case "8" -> transferRejectResponse(message);
                        // Message from Related to Main screen
                        case "12" -> handleReceiveReadyState(message);
                        case "14" -> transferClick(message);
                        case "17" -> transferContinueState(message);
                        case "18" -> transferStopRequest();
                        case "19" -> saveResult(message);
                        default -> {
                        }
                    }
                } catch (SocketException e) {
                    break;
                }
            }
        } catch(IOException | ClassNotFoundException e) {
            closeEverything();
            e.printStackTrace();
        }
    }
    
    // Home screen
    
    private void handleGetAllUserRequest(Message message) {
        List<User> users = UserDao.getAllUsers();
        sendMessage(new Message("1", users));
    }
    
    private void handleGetAllOnlineUserRequest(Message message) {
        List<User> users = new ArrayList();
        for(ClientHandler clientHandler: clientHandlers) {
            users.add(clientHandler.user);
        }
        sendMessage(new Message("2", users));
    }
    
    private void requestAddUser() {
        for(ClientHandler clientHandler: clientHandlers) {
            clientHandler.sendMessage(new Message("3", this.user));
        }
    }
    
    private void transferRemoveUserRequest() {
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler != this) {
                clientHandler.sendMessage(new Message("4" , this.user));
            }
        }
    }
    
    
    private void transferInviteRequest(Message message) {
        User receiver = (User) message.getObject();
        for(GroupHandler groupHandler: groupHandlers) {
            if(groupHandler.clientHandler1.getClientUsername().equals(receiver.getUsername()) || 
                    groupHandler.clientHandler2.getClientUsername().equals(receiver.getUsername())) {
                sendMessage(new Message("11", receiver));
                return;
            }
        }
        
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("6", this.user));
                break;
            }
        }
    }
    
    private void transferAgreementResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("9", this.user));
                
                GroupHandler groupHandler = new GroupHandler(this, clientHandler);
                groupHandlers.add(groupHandler);
                clientParner = clientHandler;
                clientHandler.clientParner = this;
                break;
            }
        }
    }
    
    private void transferRejectResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("10", this.user));
                break;
            }
        }
    }
    
    
    // Main screen
    
    private void handleReceiveReadyState(Message message) {
        isReady = true;
        if(clientParner.isReady) {
            sendGame();
        } else {
            transferReadyState();
        }
    }
    
    private void transferReadyState() {
        clientParner.sendMessage(new Message("12", user));
    }
    
    private void sendGame() {
        setUpRandomGame();
        sendMessage(new Message("13", game));
        clientParner.sendMessage(new Message("13", game));
    }
    
    
    private void transferClick(Message message) {
        Click click = (Click) message.getObject();
        int check = checkClick(click);
        if(check == 1) {
            ClickDao.createClick(click);
            sendMessage(new Message("15", click));
            clientParner.sendMessage(new Message("15", click));
        } else {
            if(check == 0) {
                sendMessage(new Message("161", click));
                clientParner.sendMessage(new Message("161", click));
            } else {
                sendMessage(new Message("162", click));
                clientParner.sendMessage(new Message("162", click));
            }
            
        }
    }
    
    private void transferContinueState(Message message) {
        if(((String) message.getObject()).equals("Yes")) {
            isContinuing = 1;
            if(clientParner.isContinuing == 1) {
                sendMessage(new Message("17", "Yes"));
                clientParner.sendMessage(new Message("17", "Yes"));
            } else if(clientParner.isContinuing == 0) {
                sendMessage(new Message("17", "No"));
                setUpAfterGame();
            }
        } else {
            isContinuing = 0;
            if(clientParner.isContinuing == 1) {
                clientParner.sendMessage(new Message("17", "No"));
                setUpAfterGame();
            }
        }
    }
    
    private void transferStopRequest() {
        clientParner.sendMessage(new Message("18"));
        setUpAfterGame();
    }
    
    
    private void saveResult(Message message) {
        Game updatedGame = (Game) message.getObject();
        if(setResult == false) {
            if(this.game.getUser1().getUsername().equals(updatedGame.getUser1().getUsername())) {
                this.game.setScore1(updatedGame.getScore1());
                this.game.setScore2(updatedGame.getScore2());
            } else {
                this.game.setScore1(updatedGame.getScore2());
                this.game.setScore2(updatedGame.getScore1());
            }
            
            if(this.game.getScore1() > this.game.getScore2()) {
                this.game.setState(1);
                UserDao.updateUser(this.game.getUser1(), "win");
                UserDao.updateUser(this.game.getUser2(), "lose");
            } else if(this.game.getScore1() < this.game.getScore2()) {
                this.game.setState(-1);
                UserDao.updateUser(this.game.getUser1(), "lose");
                UserDao.updateUser(this.game.getUser2(), "win");
            } else {
                this.game.setState(0);
                UserDao.updateUser(this.game.getUser1(), "tie");
                UserDao.updateUser(this.game.getUser2(), "tie");
            }
            
            GameDao.updateGame(this.game);
            setResult = true;
            clientParner.setResult = true;
        }
    }
    
    
    private void setUpRandomGame() {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;
        
        int gameId = GameDao.createGameWithPairId(
                user.getId(), 
                clientParner.user.getId(), 
                randomNumber
        );
        
        this.game = GameDao.getGame(    gameId);
        clientParner.game = this.game;
        
        this.points = PointDao.getAllPointWithPairId(this.game.getPair().getId());
        clientParner.points = this.points;
        this.clickedPoints.clear();
        clientParner.clickedPoints.clear();
        
    }
    
    private void setUpAfterGame() {
        for(GroupHandler groupHandler: groupHandlers) {
            if(groupHandler.clientHandler1.equals(this) || groupHandler.clientHandler1.equals(clientParner)) {
                groupHandlers.remove(groupHandler);
                break;
            }
        }
        
        this.game = null;
        clientParner.game = null;
        this.points = null;
        clientParner.points = null;
        this.clickedPoints = null;
        clientParner.clickedPoints = null;
        
        this.isReady = false;
        clientParner.isReady = false;
        this.isContinuing = -1;
        clientParner.isContinuing = -1;
        
        clientParner.clientParner = null;
        this.clientParner = null;
        
        setResult = false;
    }
    
    private int checkClick(Click click) {
        for(Point point: points) {
            if(click.getX() >= point.getMinX() && click.getX() <= point.getMaxX()
                    && click.getY() >= point.getMinY() && click.getY() <= point.getMaxY()) {
                if(clickedPoints.contains(point)) {
                    return -1;
                }
                clickedPoints.add(point);
                return 1;
            }
        }
        return 0;
    }
    
    
    
    public void removeClientHandler() {
        clientHandlers.remove(this);
    }
    
    public void closeEverything() {
        try {
            transferRemoveUserRequest();
            removeClientHandler();
            running = false;
            if(objectInputStream != null) objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(socket != null) socket.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
