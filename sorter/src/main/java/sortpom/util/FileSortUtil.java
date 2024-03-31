package sortpom.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import sortpom.parameter.PluginParameters;

public class FileSortUtil {
  private static final String XML_FILE_EXTENSION = ".xml";
  private String customSortOrderFile;
  private String predefinedSortOrder;

  public void setup(PluginParameters parameters) {
    this.customSortOrderFile = parameters.customSortOrderFile;
    this.predefinedSortOrder = parameters.predefinedSortOrder;
  }

  /**
   * Retrieves the default sort order for sortpom. A custom sort order file must always be in UTF-8
   *
   * @return Content of the default sort order file
   */
  public String getDefaultSortOrderXml() throws IOException {
    CheckedSupplier<InputStream, IOException> createStreamFunc =
        () -> {
          if (customSortOrderFile != null) {
            var urlWrapper = new UrlWrapper(customSortOrderFile);
            if (urlWrapper.isUrl()) {
              return urlWrapper.openStream();
            } else {
              return openCustomSortOrderFile();
            }
          }
          return getPredefinedSortOrder(predefinedSortOrder);
        };

    try (var inputStream = createStreamFunc.get()) {
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  /**
   * Load custom sort order file from absolute or class path.
   *
   * @return a stream to the opened resource
   */
  private InputStream openCustomSortOrderFile() throws FileNotFoundException {
    InputStream inputStream;
    try {
      inputStream = new FileInputStream(customSortOrderFile);
    } catch (FileNotFoundException ex) {
      // try classpath
      try {
        var resource = this.getClass().getClassLoader().getResource(customSortOrderFile);
        if (resource == null) {
          throw new IOException("Cannot find resource");
        }
        inputStream = resource.openStream();
      } catch (IOException e1) {
        throw new FileNotFoundException(
            String.format(
                "Could not find %s or %s in classpath",
                new File(customSortOrderFile).getAbsolutePath(), customSortOrderFile));
      }
    }
    return inputStream;
  }

  private InputStream getPredefinedSortOrder(String predefinedSortOrder) throws IOException {
    var resourceOptional =
        Optional.of(getClass())
            .map(Class::getClassLoader)
            .map(classLoader -> classLoader.getResource(predefinedSortOrder + XML_FILE_EXTENSION));

    var resource =
        resourceOptional.orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "Cannot find %s among the predefined plugin resources",
                        predefinedSortOrder + XML_FILE_EXTENSION)));

    return resource.openStream();
  }
}
