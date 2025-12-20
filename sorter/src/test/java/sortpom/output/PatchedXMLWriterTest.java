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
        """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE xml [
              <!ENTITY copy "©">
              <!ENTITY trade "™">
              <!ENTITY deg "°">
              <!ENTITY gt "&#62;">
              <!ENTITY sup2 "²">
              <!ENTITY frac14 "¼">
              <!ENTITY quot "&#34;">
              <!ENTITY frac12 "½">
              <!ENTITY euro "€">
              <!ENTITY Omega "Ω">
            ]><root/>""";

    var reader = new SAXReader();
    reader.setIncludeInternalDTDDeclarations(true);

    var wr = new StringWriter();
    var writer = new PatchedXMLWriter(wr, new OutputFormat(), false, false, null, false);
    writer.write(reader.read(new StringReader(xml)));

    assertEquals(xml, wr.toString());
  }
}
