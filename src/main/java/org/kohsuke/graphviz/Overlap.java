package org.kohsuke.graphviz;

/**
 * c.f. https://www.graphviz.org/doc/info/attrs.html#d:overlap
 * @author Valentin Kuhn
 */
public enum Orientation {
    TRUE, FALSE, SCALE, PRISM, SCALEXY, COMPRESS, VPSC,
    @Deprecated ORTHO, @Deprecated ORTHOXY, @Deprecated ORTHOYX, @Deprecated ORTHO_YX,
    @Deprecated PORTHO, @Deprecated PORTHOXY, @Deprecated PORTHOYX, @Deprecated PORTHO_YX
}
