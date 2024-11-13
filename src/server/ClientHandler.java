/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import helper.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Click;
import server.dao.ClickDao;
import model.Game;
import server.dao.GameDao;
import model.Point;
import server.dao.PointDao;
import model.User;
import server.dao.UserDao;

public class ClientHandler implements Runnable{
    private static final List<ClientHandler> clientHandlers = new ArrayList<>();
    private static final List<GroupHandler> groupHandlers = new ArrayList<>();
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
            if(user != null) {
                requestAddUserToOnlineUsers();
                clientHandlers.add(this);
            }
        } catch(Exception ex) {
            closeEverything();
            ex.printStackTrace();
        }
    }

    public String getClientUsername() {
        if(user == null) return "null";
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
                        // Message from Auth screen
                        case "RQ-REGISTER" -> handleRegisterRequest(message);
                        case "RQ-LOGIN" -> handleLoginRequest(message);
                        // Message from Home screen
                        case "RQ-CLOSE" -> closeEverything();
                        case "RQ-USER" -> handleGetUserRequest(message);
                        case "RQ-USERS" -> handleGetAllUsersRequest(message);
                        case "RQ-ONLINE" -> handleGetAllOnlineUsersRequest(message);
                        case "RQ-REMOVE" -> transferRemoveUserFromOnlineUsersRequest();
                        case "RQ-INVITE" -> transferInviteRequest(message);
                        case "RP-AGREE" -> transferAgreementResponse(message);
                        case "RP-REJECT" -> transferRejectResponse(message);
                        // Message from Main screen
                        case "RQ-READY" -> handleReceiveReadyState(message);
                        case "RQ-CLICK" -> handleReceiveClick(message);
                        case "RQ-ENDGAME" -> handleEndGame(message);
                        case "RQ-CONTINUE" -> handleReceiveContinueState(message);
                        case "RQ-STOP" -> handleStopRequest();
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
    
    // Auth screen
    
    private void handleRegisterRequest(Message message) {
        User user = (User) message.getObject();
        User existedUser = UserDao.getUserByName(user.getUsername());
        if(existedUser != null) {
            sendMessage(new Message("RP-REGISTER", null));
        } else {
            User newUser = UserDao.createUser(user);
            sendMessage(new Message("RP-REGISTER", newUser));
            
            List<User> users = UserDao.getAllUsers();
            for(ClientHandler clientHandler: clientHandlers) {
                clientHandler.sendMessage(new Message("RP-USERS", users));
            }
        }
    }
    
    private void handleLoginRequest(Message message) {
        User user = (User) message.getObject();
        User existedUser = UserDao.getUserByName(user.getUsername());
        sendMessage(new Message("RP-LOGIN", existedUser));
    }
    
    
    // Home screen
    
    private void handleGetUserRequest(Message message) {
        this.user = UserDao.getUser(this.user.getId());
        sendMessage(new Message("RP-USER", UserDao.getUser(this.user.getId())));
    }
    
    private void handleGetAllUsersRequest(Message message) {
        List<User> users = UserDao.getAllUsers();
        sendMessage(new Message("RP-USERS", users));
    }
    
    private void handleGetAllOnlineUsersRequest(Message message) {
        List<User> users = new ArrayList();
        for(ClientHandler clientHandler: clientHandlers) {
            users.add(clientHandler.user);
        }
        sendMessage(new Message("RP-ONLINE", users));
    }
    
    private void requestAddUserToOnlineUsers() {
        for(ClientHandler clientHandler: clientHandlers) {
            clientHandler.sendMessage(new Message("RQ-ADD", this.user));
        }
    }
    
    private void transferRemoveUserFromOnlineUsersRequest() {
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler != this) {
                clientHandler.sendMessage(new Message("RQ-REMOVE" , this.user));
            }
        }
    }
    
    
    private void transferInviteRequest(Message message) {
        User receiver = (User) message.getObject();
        for(GroupHandler groupHandler: groupHandlers) {
            if(groupHandler.clientHandler1.getClientUsername().equals(receiver.getUsername()) || 
                    groupHandler.clientHandler2.getClientUsername().equals(receiver.getUsername())) {
                sendMessage(new Message("RP-BUSY", receiver));
                return;
            }
        }
        
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("RQ-INVITE", this.user));
                break;
            }
        }
    }
    
    private void transferAgreementResponse(Message message) {
        User receiver = (User) message.getObject();
        for(ClientHandler clientHandler: clientHandlers) {
            if(clientHandler.user.getUsername().equals(receiver.getUsername())) {
                clientHandler.sendMessage(new Message("RP-AGREE", this.user));
                
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
                clientHandler.sendMessage(new Message("RP-REJECT", this.user));
                break;
            }
        }
    }
    
    
    // Main screen
    
    private void handleReceiveReadyState(Message message) {
        isReady = true;
        if(clientParner.isReady) {
            setUpRandomGame();
            sendMessage(new Message("RP-GAME", game));
            clientParner.sendMessage(new Message("RP-GAME", game));
        } else {
            clientParner.sendMessage(new Message("RQ-READY", user));
        }
    }
    
    private void handleReceiveClick(Message message) {
        Click click = (Click) message.getObject();
        int check = checkClick(click);
        if(check == 1) {
            ClickDao.createClick(click);
            sendMessage(new Message("RP-RIGHTCLICK", click));
            clientParner.sendMessage(new Message("RP-RIGHTCLICK", click));
        } else {
            if(check == 0) {
                sendMessage(new Message("RP-FAILCLICK1", click));
                clientParner.sendMessage(new Message("RP-FAILCLICK1", click));
            } else {
                sendMessage(new Message("RP-FALICLICK2", click));
                clientParner.sendMessage(new Message("RP-FALICLICK2", click));
            }
            
        }
    }
    
    private void handleEndGame(Message message) {
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
    
    private void handleReceiveContinueState(Message message) {
        if(((String) message.getObject()).equals("Yes")) {
            isContinuing = 1;
            if(clientParner.isContinuing == 1) {
                sendMessage(new Message("RP-CONTINUE", "Yes"));
                clientParner.sendMessage(new Message("RP-CONTINUE", "Yes"));
                resetGame();
                clientParner.resetGame();
            } else if(clientParner.isContinuing == 0) {
                sendMessage(new Message("RP-CONTINUE", "No"));
                clearGame();
            }
        } else {
            isContinuing = 0;
            if(clientParner.isContinuing == 1) {
                clientParner.sendMessage(new Message("RP-CONTINUE", "No"));
                clearGame();
            } else if(clientParner.isContinuing == 0) {
                clearGame();
            }
        }
    }
    
    
    private void handleStopRequest() {
        clientParner.sendMessage(new Message("RQ-STOP"));
        clearGame();
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
    }
    
    private void clearGame() {
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
        
        this.clickedPoints.clear();
        clientParner.clickedPoints.clear();
        
        this.isReady = false;
        clientParner.isReady = false;
        
        this.isContinuing = -1;
        clientParner.isContinuing = -1;
        
        setResult = false;
        this.setResult = false;
        
        clientParner.clientParner = null;
        this.clientParner = null;
    }
    
    private void resetGame() {
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
        
        this.clickedPoints.clear();
        clientParner.clickedPoints.clear();
        
        this.isReady = false;
        clientParner.isReady = false;
        
        this.isContinuing = -1;
        clientParner.isContinuing = -1;
        
        setResult = false;
        this.setResult = false;
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
            if(user != null) {
                transferRemoveUserFromOnlineUsersRequest();
                removeClientHandler();
                running = false;
            }
            if(objectInputStream != null) objectInputStream.close();
            if(objectOutputStream != null) objectOutputStream.close();
            if(socket != null) socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}