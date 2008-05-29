package org.kohsuke.graphviz;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Kohsuke Kawaguchi
 */
public class Graph extends GraphObject<Graph> {
    private final List<Node> nodes = new ArrayList<Node>();
    private final List<Edge> edges = new ArrayList<Edge>();
    private final List<Graph> subGraphs = new ArrayList<Graph>();

    private Style with;

    /**
     * Sets the style to be used in successive {@code node} or {@code edge}
     * invocations.
     */
    public Graph with(Style s) {
        with = s;
        return this;
    }

    /**
     * Applies the current style if any.
     */
    private <T extends GraphObject> T decorate(T t) {
        if(with!=null && t.style()==null)  t.style(with);
        return t;
    }

    public Graph node(Node n) {
        nodes.add(decorate(n));
        return this;
    }

    public Graph node(String label, Style s) {
        return node(new Node().attr("label",label).style(s));
    }

    public Graph edge(Edge e) {
        edges.add(decorate(e));
        return this;
    }

    public Graph edge(Node src, Node dst) {
        return edge(src,dst,null);
    }

    public Graph edge(Node src, Node dst, Style s) {
        return edge(new Edge(src,dst).style(s));
    }

    Graph self() {
        return this;
    }

    /**
     * Writes out this graph in the dot format.
     */
    public void writeTo(OutputStream out) {
        writeTo(new Printer(out));
    }

    private void writeTo(Printer out) {
        out.println("digraph "+out.id(this)+" {");
        writeNodes(out);
        writeEdges(out);
        out.println("}");
    }

    private void writeNodes(Printer out) {
        // write attributes
        Map<String,String> m = getEffectiveAttributes();
        for (Entry<String, String> e : m.entrySet()) {
            out.print(e.getKey());
            out.print('=');
            out.println(escape(e.getValue()));
        }

        for (Node n : nodes)
            n.write(out);

        for (Graph g : subGraphs) {
            out.println("subgraph "+out.id(this)+" {");
            g.writeNodes(out);
            out.println("}");
        }
    }

    private void writeEdges(Printer out) {
        for (Edge e : edges)
            e.write(out);
        for (Graph g : subGraphs)
            g.writeEdges(out);
    }
}
