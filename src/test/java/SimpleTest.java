import junit.framework.TestCase;
import static org.kohsuke.graphviz.Attribute.COLOR;
import org.kohsuke.graphviz.Graph;
import org.kohsuke.graphviz.Style;

import java.awt.*;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;

public class SimpleTest extends TestCase {
    public void test1() throws IOException, InterruptedException {
        Graph graph = new Graph();
        Style s = new Style();
        s.attr(COLOR, Color.RED);
        graph.nodeWith(s).node("a").to().node("b").generateTo(Arrays.asList("dot","-Tgif"),new File("test.gif"));
    }
}