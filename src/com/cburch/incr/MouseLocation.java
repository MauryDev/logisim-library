package com.cburch.incr;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.Calendar;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringGetter;
import com.cburch.logisim.util.StringUtil;

public class MouseLocation extends InstanceFactory {
    public static final BitWidth DEFAULT_WIDTH1 = BitWidth.create(11);
    public int x = 0;
    public int y = 0;
    public Boolean oldclock = Boolean.FALSE;

    public MouseLocation() {
        super("MouseLocation");
        setOffsetBounds(Bounds.create(-30, -15, 30, 30));
        Port[] ps = new Port[3];
        ps[0] = new Port(-30, 0, "input", BitWidth.ONE);
        ps[1] = new Port(0, -10, "output", DEFAULT_WIDTH1);
        ps[2] = new Port(0, 10, "output", DEFAULT_WIDTH1);
        ps[0].setToolTip(StringUtil.constantGetter("clk"));
        ps[1].setToolTip(StringUtil.constantGetter("X"));
        ps[2].setToolTip(StringUtil.constantGetter("Y"));

        setPorts(ps);

    }

    @Override
    public void propagate(InstanceState state) {
        Value in = state.getPort(0);
        if (in.isFullyDefined()) {
            Boolean currentclock = in == Value.TRUE;

            if (!oldclock && currentclock) {
                PointerInfo info = MouseInfo.getPointerInfo();
                Point point = info.getLocation();
                x = point.x;
                y = point.y;

            }

            oldclock = currentclock;

        }

        Value out = Value.createKnown(DEFAULT_WIDTH1, x);
        Value out2 = Value.createKnown(DEFAULT_WIDTH1, y);

        state.setPort(1, out, DEFAULT_WIDTH1.getWidth());
        state.setPort(2, out2, DEFAULT_WIDTH1.getWidth());

    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        painter.drawBounds();

        painter.drawPorts();

        GraphicsUtil.drawCenteredText(g, "ML",
				bds.getX() + bds.getWidth() / 2,
				bds.getY() + bds.getHeight() / 2);
    }
}
