package org.kohsuke.graphviz;

import java.util.Map;
import java.util.HashMap;
import java.io.PrintStream;
import java.io.OutputStream;

/**
 * @author Kohsuke Kawaguchi
 */
final class Printer extends PrintStream {
    private final Map<GraphObject,String> idTable = new HashMap<GraphObject, String>();

    Printer(OutputStream out) {
        super(out);
    }

    /**
     * Assigns a unique ID to the given object.
     */
    String id(GraphObject o) {
        String id = idTable.get(o);
        if(id==null)
            idTable.put(o,id="n"+idTable.size());
        return id;
    }
}
