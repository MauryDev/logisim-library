package com.cburch.incr;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.StringUtil;
import java.awt.Graphics;
import java.lang.Math;

public class DecimalParts extends InstanceFactory {
    public static final BitWidth DEFAULT_WIDTH1 = BitWidth.create(8);
    public static final BitWidth DEFAULT_WIDTH2 = BitWidth.create(4);
    public static final Attribute<BitWidth> attribute = Attributes.forBitWidth("Data Bits", 1, 32);

    public DecimalParts() {
        super("DecimalParts",StringUtil.constantGetter("Decimal Parts"));

        setAttributes(new Attribute[] {
                attribute
        }, new Object[] {
                DEFAULT_WIDTH1
        });
        updatePortsStd();
        

    }
    @Override
    public void propagate(InstanceState state) {
        Value in = state.getPort(0);
        if (in.isFullyDefined()) {
            BitWidth val = (BitWidth)state.getAttributeValue(attribute);
            int size = OutPortsLen(val.getWidth());
            int maxpow = (int)Math.pow(10, size - 1);

            int v = in.toIntValue();
            for (int i = 0; i < size; i++) {
                int valcurrent = v / maxpow;
                v = v - (valcurrent * maxpow);
                state.setPort(i + 1, Value.createKnown(DEFAULT_WIDTH2, valcurrent), 5);
                maxpow /= 10;
            }
            
        }
    }

    @Override
    public Bounds getOffsetBounds(AttributeSet attrs) {
        BitWidth val = (BitWidth) attrs.getValue(attribute);
        int size = val.getWidth();
        int dsize = OutPortsLen(size);
        int ht = ((dsize - 1) * 20) + 10;
        int ht_metade = -(ht / 2);

        return Bounds.create(-30, ht_metade, 30,ht);
    }

    @Override
    protected void configureNewInstance(Instance instance) {
        super.configureNewInstance(instance);
        instance.addAttributeListener();
        BitWidth val = (BitWidth) instance.getAttributeValue(attribute);
        updatePorts(instance,val.getWidth());
    }

    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        super.instanceAttributeChanged(instance, attr);

        if (attr == attribute) {
            BitWidth val = (BitWidth)instance.getAttributeValue(attribute);
            int size = val.getWidth();
            instance.recomputeBounds();
            updatePorts(instance,size);
        }
    }

    public void updatePorts(Instance instance,int size) {
        int dsize = OutPortsLen(size);
        int ht = ((dsize - 1) * 20) + 10;
        int ht_metade = -(ht / 2);

        Port[] ps = new Port[1 + dsize];

        ps[0] = new Port(-30, 0, "input", attribute);
        ps[0].setToolTip(StringUtil.constantGetter("Value"));
        int start = ht_metade + 5;
        for (int index = 1; index < ps.length; index++) {
            ps[index] = new Port(0, start, "output", DEFAULT_WIDTH2);
            ps[index].setToolTip(StringUtil.constantGetter("U" + index));

            start += 20;
        }

        instance.setPorts(ps);
    }

    public void updatePortsStd() {
        setOffsetBounds(Bounds.create(-30, -25, 30, 50));

        Port[] ps = new Port[4];

        ps[0] = new Port(-30, 0, "input", attribute);
        ps[0].setToolTip(StringUtil.constantGetter("Value"));

        ps[1] = new Port(0, -20, "output", DEFAULT_WIDTH2);
        ps[1].setToolTip(StringUtil.constantGetter("U1"));

        ps[2] = new Port(0, 0, "output", DEFAULT_WIDTH2);
        ps[2].setToolTip(StringUtil.constantGetter("U2"));

        ps[3] = new Port(0, 20, "output", DEFAULT_WIDTH2);
        ps[3].setToolTip(StringUtil.constantGetter("U3"));

        setPorts(ps);
    }
    int OutPortsLen(int size)
    {
        int dsize = 0;
        if (size <= 3) {
            dsize = 1;
        } else if (size <= 6) {
            dsize = 2;
        } else if (size <= 9) {
            dsize = 3;
        } else if (size <= 13) {
            dsize = 4;
        } else if (size <= 16) {
            dsize = 5;
        } else if (size <= 19) {
            dsize = 6;
        } else if (size <= 23) {
            dsize = 7;
        } else if (size <= 26) {
            dsize = 8;
        } else if (size <= 29) {
            dsize = 9;
        } else if (size <= 32) {
            dsize = 10;
        }
        return dsize;
    }
    @Override
    public void paintInstance(InstancePainter painter) {

        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        painter.drawBounds();

        painter.drawPorts();

        GraphicsUtil.drawCenteredText(g, "DP",
                bds.getX() + bds.getWidth() / 2,
                bds.getY() + bds.getHeight() / 2);
    }
}
