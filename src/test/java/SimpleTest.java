import junit.framework.TestCase;
import static org.kohsuke.graphviz.Attribute.COLOR;
import org.kohsuke.graphviz.*;

import java.awt.*;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class SimpleTest extends TestCase {
    public void test1() throws IOException, InterruptedException {
        Graph graph = new Graph();
        Style s = new Style();
        s.attr(COLOR, Color.RED);
        graph.nodeWith(s).node("a").to().node("b").generateTo(Arrays.asList("dot","-Tgif"),new File("test1.gif"));
    }

    public void test2() throws IOException, InterruptedException {
        Graph graph = new Graph();
        Style s = new Style();
        s.attr("html", "<TABLE BORDER=\"0\"><TR><TD><IMG SRC=\"package.png\" /></TD></TR><TR><TD>caption</TD></TR></TABLE>");
        s.attr(Attribute.FONTNAME,"sans serif");
        s.attr(Attribute.FONTSIZE,10f);
        s.attr(Attribute.SHAPE, org.kohsuke.graphviz.Shape.NONE);
        graph.nodeWith(s);
        graph.node("a").to().node("b").to().node("c");
        graph.generateTo(Arrays.asList("dot","-Tgif"),new File("test2.gif"));
    }
}