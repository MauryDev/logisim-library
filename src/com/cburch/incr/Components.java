package com.cburch.incr;

import java.util.ArrayList;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

public class Components extends Library {

    private ArrayList<Tool> tools;

    public Components() {
        try {
            tools = new ArrayList<Tool>();
            tools.add(new AddTool(new DecimalParts()));
            tools.add(new AddTool(new Timepiece()));
            tools.add(new AddTool(new MouseLocation()));

                
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
        
    }

    public String getName() {
        return Components.class.getName();
    }

    public String getDisplayName() {
        return "Tools";
    }

    public List<Tool> getTools() {
        return tools;
    }
}
