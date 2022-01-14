enum TrafficLightColor {
    RED, YELLOW, GREEN
}

class AutomaticTrafficLight implements Runnable {
    private TrafficLightColor tlc; // stores current light color
    private TrafficLightColor ptlc; // stores previous light color
    boolean stop = false; // stops executing app when true
    boolean changed = false; // value changes to true when the light switches

    AutomaticTrafficLight(TrafficLightColor init) { tlc = init; }
    AutomaticTrafficLight() { tlc = TrafficLightColor.RED; }

    // starts running the simulation
    public void run() {
        while (!stop) {
            try {
                switch (tlc) {
                    case GREEN:
                        Thread.sleep(2000);
                        break;
                    case YELLOW:
                        Thread.sleep(1000);
                        break;
                    case RED:
                        Thread.sleep(2200);
                        break;
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            changeColor();
        }
    }
    // changes the color
    synchronized void changeColor() {
        switch (tlc) {
            case RED:
                ptlc = TrafficLightColor.RED;
                tlc = TrafficLightColor.YELLOW;
                break;
            case YELLOW:
                if (ptlc == TrafficLightColor.RED) {
                    tlc = TrafficLightColor.GREEN;
                }
                if (ptlc == TrafficLightColor.GREEN) {
                    tlc = TrafficLightColor.RED;
                }
                break;
            case GREEN:
                ptlc = TrafficLightColor.GREEN;
                tlc = TrafficLightColor.YELLOW;
                break;
        }
        changed = true;
        notify();
    }
    // waits for the light to change
    synchronized void waitForChange() {
        try {
            while (!changed)
                wait();
            changed = false;
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
    // returns current light color
    synchronized TrafficLightColor getColor() { return tlc; }
    // stops the simulation
    synchronized void cancel() { stop = true; }
}

public class Main {

    public static void main(String[] args) {
        // runs the app
        AutomaticTrafficLight t1 = new AutomaticTrafficLight(TrafficLightColor.GREEN);

        Thread thread = new Thread(t1);
        thread.start();

        for (int i = 0; i < 9; i++) {
            System.out.println(t1.getColor());
            t1.waitForChange();
        }
        t1.cancel();
    }
}
