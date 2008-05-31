package org.kohsuke.graphviz;

import java.io.OutputStream;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Map.Entry;

/**
 * Represents a graph.
 *
 * @author Kohsuke Kawaguchi
 */
public class Graph extends GraphObject<Graph> {
    private final List<Node> nodes = new ArrayList<Node>();
    private final List<Edge> edges = new ArrayList<Edge>();
    private final List<Graph> subGraphs = new ArrayList<Graph>();

    private Style nodeWith,edgeWith;

    private boolean to;

    /**
     * Sets the style to be used in successive {@code node(...)}
     * invocations.
     */
    public Graph nodeWith(Style s) {
        nodeWith = s;
        return this;
    }

    /**
     * Sets the style to be used in successive {@code edge(...)}
     * invocations.
     */
    public Graph edgeWith(Style s) {
        edgeWith = s;
        return this;
    }

    /**
     * Applies the current style if any.
     */
    private <T extends GraphObject> T decorate(T t, Style with) {
        if(with!=null && t.style()==null)  t.style(with);
        return t;
    }

    public Graph node(Node n) {
        nodes.add(decorate(n,nodeWith));
        if(to) {
            to = false;
            int sz = nodes.size();
            edge(nodes.get(sz-2),nodes.get(sz -1));
        }
        return this;
    }

    public Graph node(String label) {
        return node(label,null);
    }

    public Graph node(String label, Style s) {
        return node(new Node().attr("label",label).style(s));
    }

    /**
     * Used between two invocations of the node method
     * to insert a new edge between them.
     */
    public Graph to() {
        to = true;
        return this;
    }

    public Graph edge(Edge e) {
        edges.add(decorate(e,edgeWith));
        return this;
    }

    public Graph edge(Node src, Node dst) {
        return edge(src,dst,null);
    }

    public Graph edge(Node src, Node dst, Style s) {
        return edge(new Edge(src,dst).style(s));
    }

    public Graph subGraph(Graph g) {
        subGraphs.add(g);
        return this;
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

    /**
     * Generates the graph into the specified {@link OutputStream}.
     *
     * @param commands
     *      The complete dot invocation and format specifier, such as ["dot","-Tgif"].
     */
    public void generateTo(List<String> commands, OutputStream out) throws InterruptedException, IOException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        Process proc = pb.start();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        StreamCopyThread t1 = new StreamCopyThread("stderr for " + Arrays.asList(commands),
                proc.getErrorStream(), error);
        StreamCopyThread t2 = new StreamCopyThread("stdout for " + Arrays.asList(commands),
                proc.getInputStream(), out);
        t1.start();
        t2.start();

        writeTo(proc.getOutputStream());
        proc.getOutputStream().close();

        int r = proc.waitFor();
        t1.join();
        t2.join();
        if(r!=0)
            throw new IllegalArgumentException(error.toString());
    }

    /**
     * Generates the graph into the specified file.
     */
    public void generateTo(List<String> commands, File f) throws InterruptedException, IOException {
        commands = new ArrayList<String>(commands);
        commands.add("-o");
        commands.add(f.getAbsolutePath());
        generateTo(commands,new ByteArrayOutputStream());
    }
}
