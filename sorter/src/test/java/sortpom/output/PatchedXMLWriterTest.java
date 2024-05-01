package sortpom.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;

class PatchedXMLWriterTest {

  /** Copied, with gratitude, from Dom4J */
  @Test
  void writeXmlWithDocType() throws Exception {
    var xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE xml [\n"
            + "  <!ENTITY copy \"©\">\n"
            + "  <!ENTITY trade \"™\">\n"
            + "  <!ENTITY deg \"°\">\n"
            + "  <!ENTITY gt \"&#62;\">\n"
            + "  <!ENTITY sup2 \"²\">\n"
            + "  <!ENTITY frac14 \"¼\">\n"
            + "  <!ENTITY quot \"&#34;\">\n"
            + "  <!ENTITY frac12 \"½\">\n"
            + "  <!ENTITY euro \"€\">\n"
            + "  <!ENTITY Omega \"Ω\">\n"
            + "]><root/>";

    var reader = new SAXReader();
    reader.setIncludeInternalDTDDeclarations(true);

    var wr = new StringWriter();
    var writer = new PatchedXMLWriter(wr, new OutputFormat(), false, false, null, false);
    writer.write(reader.read(new StringReader(xml)));

    assertEquals(xml, wr.toString());
  }
}
