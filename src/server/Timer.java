/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import helper.Message;

public class Timer implements Runnable {
    private GroupHandler groupHandler;

    public Timer(GroupHandler groupHandler) {
        this.groupHandler = groupHandler;
    }
    
    @Override
    public void run() {
        int count = 30;
        while (count > 0) {
            try {
                Thread.sleep(1000);
                count--;
//                System.out.println(count);
                groupHandler.clientHandler1.sendMessage(new Message("TIMER-COUNT", count));
                groupHandler.clientHandler2.sendMessage(new Message("TIMER-COUNT", count));
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String args[]) {
//        Thread thread = new Thread(new Timer());
//        thread.start();
    }
}
