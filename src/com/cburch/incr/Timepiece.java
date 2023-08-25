package com.cburch.incr;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Calendar;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.tools.key.BitWidthConfigurator;
import com.cburch.logisim.tools.key.KeyConfigurator;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringUtil;

public class Timepiece extends InstanceFactory {
  public static final int CLOCK = 0;
  public static final int HoursPin = 1;
  public static final int MinutesPin = 2;
  public static final int SecondsPin = 3;


  public Boolean oldclock = Boolean.FALSE;
  public int hours = 0;
  public int minutes = 0;
  public int seconds = 0;

  public static final BitWidth WIDTH_DEFAULT = BitWidth.create(6);
  public static final BitWidth WIDTH_DEFAULT2 = BitWidth.create(5);


  public Timepiece() {
    super("Timepiece", StringUtil.constantGetter("Timepiece"));
    setOffsetBounds(Bounds.create(-30, -15, 30, 30));
    Port[] ps = new Port[4];
    
    ps[0] = new Port(-30, 0, "input", BitWidth.ONE);
    ps[1] = new Port(0, -10, "output", WIDTH_DEFAULT2);
    ps[2] = new Port(0, 0, "output", WIDTH_DEFAULT);
    ps[3] = new Port(0, 10, "output", WIDTH_DEFAULT);

    ps[0].setToolTip(StringUtil.constantGetter("Clock"));
    ps[1].setToolTip(StringUtil.constantGetter("Hours"));
    ps[2].setToolTip(StringUtil.constantGetter("Minutes"));
    ps[3].setToolTip(StringUtil.constantGetter("Seconds"));

    setPorts(ps);
  }
  @Override
  public void propagate(InstanceState state) {

    Value in = state.getPort(CLOCK);
    if (in.isFullyDefined()) {
      Boolean currentclock = in == Value.TRUE;

      if (!oldclock && currentclock) {
        Calendar rightNow = Calendar.getInstance();
        hours = rightNow.get(Calendar.HOUR_OF_DAY);
        minutes = rightNow.get(Calendar.MINUTE);
        seconds = rightNow.get(Calendar.SECOND);
      }

      oldclock = currentclock;

    }

    Value out = Value.createKnown(WIDTH_DEFAULT2, hours);
    Value out2 = Value.createKnown(WIDTH_DEFAULT, minutes);
    Value out3 = Value.createKnown(WIDTH_DEFAULT, seconds);

    state.setPort(HoursPin,out,WIDTH_DEFAULT2.getWidth());
    state.setPort(MinutesPin,out2,WIDTH_DEFAULT.getWidth());
    state.setPort(SecondsPin,out3,WIDTH_DEFAULT.getWidth());

  }
  @Override
  public void paintInstance(InstancePainter painter) {
    Graphics g = painter.getGraphics();
    Bounds bds = painter.getBounds();
    painter.drawBounds();

    painter.drawPorts();

    GraphicsUtil.drawCenteredText(g, "Time",
				bds.getX() + bds.getWidth() / 2,
				bds.getY() + bds.getHeight() / 2);
  }
}