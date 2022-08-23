package cc.polyfrost.polyaccessibility.utils;

import cc.polyfrost.polyaccessibility.PolyAccessibility;

import java.util.Map;

public class PolyThread extends Thread {
    private boolean running = false;

    public void run() {
        while (running) {
            try {
                if (!PolyAccessibility.delayThreadMap.isEmpty()) {
                    for (Map.Entry<String, Integer> entry : PolyAccessibility.delayThreadMap.entrySet()) {
                        entry.setValue(entry.getValue()-1);
                        if (entry.getValue()<=10) {
                            System.out.println("removed " + entry.getKey() + " from PolyThread.");
                            PolyAccessibility.delayThreadMap.remove(entry.getKey());
                        }
                    }
                }
                Thread.sleep(1);
            } catch (Exception ignored) {
            }
        }
    }

    public void stopThread() {
        running = false;
        interrupt();
    }

    public void startThread() {
        running = true;
        this.start();
    }
}
