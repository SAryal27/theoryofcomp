// DFAPanel.java
// Java2D panel to draw DFA states and transitions with circular layout and curved self-loops
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

public class DFAPanel extends JPanel {
    private DFA dfa; //structural + runtime info (positions stored in State)

    public DFAPanel() {
        setBackground(Color.WHITE);
    }

    public void setDFA(DFA d) {
        this.dfa = d;
        layoutStatesCircle();
        repaint();
    }

    //compute circular layout based on current states
    public void layoutStatesCircle() {
        if (dfa == null) return;
        int n = dfa.getStates().size();
        if (n == 0) return;
        int w = Math.max(getWidth(), 300);
        int h = Math.max(getHeight(), 300);
        int radius = Math.min(w, h) / 2 - 80;
        int cx = w / 2;
        int cy = h / 2;
        int i = 0;
        for (State s : dfa.getStates()) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            s.setPosition(x, y);
            i++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dfa == null) {
            g.setColor(Color.DARK_GRAY);
            g.drawString("No DFA built yet. Fill fields and click Simulate/Step.", 10, 20);
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw transitions first so states are on top
        drawTransitions(g2);

        // draw states
        for (State s : dfa.getStates()) {
            drawState(g2, s);
        }

        g2.dispose();
    }

    private void drawTransitions(Graphics2D g2) {
        Map<String,HashMap<Character,String>> trans = dfa.getTransitions();
        if (trans == null) return;
        for (String src : trans.keySet()) {
            Map<Character, String> row = trans.get(src);
            if (row == null) continue;
            State from = dfa.getState(src);
            if (from == null) continue;
            for (Map.Entry<Character, String> e : row.entrySet()) {
                Character sym = e.getKey();
                String dstName = e.getValue();
                State to = dfa.getState(dstName);
                if (to == null) continue;
                drawArrowForTransition(g2, from, to, sym);
            }
        }
    }

    private void drawArrowForTransition(Graphics2D g2, State from, State to, Character sym) {
        int x1 = from.getX(), y1 = from.getY();
        int x2 = to.getX(), y2 = to.getY();
        int r = 30;

        //self-loop: draw curved loop above the state
        if (from.getName().equals(to.getName())) {
            int loopRadius = 18;
            int lx = x1;
            int ly = y1 - r - loopRadius;
            g2.setColor(Color.BLACK);
            g2.drawOval(lx - loopRadius, ly - loopRadius, loopRadius * 2, loopRadius * 2);
            g2.drawString(sym.toString(), lx + loopRadius, ly);
            return;
        }

        //normal arrow from from -> to
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dist = Math.hypot(dx, dy);
        if (dist < 1) dist = 1;

        //shorten so circle edges not overlapped
        double ux = dx / dist;
        double uy = dy / dist;
        int sx = (int) (x1 + ux * r);
        int sy = (int) (y1 + uy * r);
        int ex = (int) (x2 - ux * r);
        int ey = (int) (y2 - uy * r);

        //draw a slightly curved line (use QuadCurve)
        int mx = (sx + ex) / 2;
        int my = (sy + ey) / 2;

        //control point for curve: perpendicular offset
        double px = -uy;
        double py = ux;
        double curvature = 30; // adjust for curve strength
        int cx = (int) (mx + px * curvature);
        int cy = (int) (my + py * curvature);

        QuadCurve2D q = new QuadCurve2D.Float();
        q.setCurve(sx, sy, cx, cy, ex, ey);
        g2.setColor(Color.BLACK);
        g2.draw(q);

        //arrowhead: compute tangent at end point
        //approximate derivative at t = 1: derivative = (end - control)
        double tx = ex - cx;
        double ty = ey - cy;
        double theta = Math.atan2(ty, tx);
        drawArrowHead(g2, ex, ey, theta);

        //label near curve midpoint (t = 0.5)
        double t = 0.5;
        double qx = (1 - t)*(1 - t)*sx + 2*(1 - t)*t*cx + t*t*ex;
        double qy = (1 - t)*(1 - t)*sy + 2*(1 - t)*t*cy + t*t*ey;
        g2.drawString(sym.toString(), (int) qx + 6, (int) qy - 6);
    }

    private void drawArrowHead(Graphics2D g2, int x, int y, double theta) {
        int barb = 10;
        double phi = Math.toRadians(20);
        int x1 = (int) (x - barb * Math.cos(theta + phi));
        int y1 = (int) (y - barb * Math.sin(theta + phi));
        int x2 = (int) (x - barb * Math.cos(theta - phi));
        int y2 = (int) (y - barb * Math.sin(theta - phi));
        g2.drawLine(x, y, x1, y1);
        g2.drawLine(x, y, x2, y2);
    }

    private void drawState(Graphics2D g2, State s) {
        int x = s.getX(), y = s.getY();
        int r = 30;
        //highlight current
        String curr = dfa.getCurrentState();
        if (curr != null && curr.equals(s.getName())) {
            g2.setColor(Color.ORANGE);
            g2.fillOval(x - r, y - r, 2 * r, 2 * r);
        }

        //outline
        g2.setColor(Color.BLACK);
        g2.drawOval(x - r, y - r, 2 * r, 2 * r);

        //double circle if accept
        if (s.isAccept()) {
            int inner = 6;
            g2.drawOval(x - r + inner, y - r + inner, 2 * (r - inner), 2 * (r - inner));
        }

        // start arrow marker
        if (s.isStart()) {
            int sx = x - r - 20;
            int sy = y;
            g2.drawLine(sx, sy, x - r, y);
            double theta = Math.atan2(y - sy, (x - r) - sx);
            drawArrowHead(g2, x - r, y, theta);
        }

        //fill white behind text when not highlighted
        if (!(curr != null && curr.equals(s.getName()))) {
            g2.setColor(Color.WHITE);
            g2.fillOval(x - r + 1, y - r + 1, 2 * r - 1, 2 * r - 1);
            g2.setColor(Color.BLACK);
            g2.drawOval(x - r, y - r, 2 * r, 2 * r);
        }

        //draw name of state
        FontMetrics fm = g2.getFontMetrics();
        String name = s.getName();
        int tw = fm.stringWidth(name);
        g2.drawString(name, x - tw / 2, y + fm.getAscent() / 2);
    }
}
