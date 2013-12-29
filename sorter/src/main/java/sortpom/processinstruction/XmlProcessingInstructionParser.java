package sortpom.processinstruction;

import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;

/**
 * @author bjorn
 * @since 2013-12-28
 */
public class XmlProcessingInstructionParser {
    private IgnoredSectionsMap ignoredSectionsMap = new IgnoredSectionsMap();
    private String originalXml;
    private SortPomLogger logger;
    private boolean containsIgnoredSections = false;

    public void setup(SortPomLogger logger) {
        this.logger = logger;
    }

    public void scanForIgnoredSections(String originalXml) {
        this.originalXml = originalXml;
        SortpomPiScanner sortpomPiScanner = new SortpomPiScanner(logger);
        sortpomPiScanner.scan(originalXml);
        if (sortpomPiScanner.isScanError()) {
            throw new FailureException(sortpomPiScanner.getFirstError());
        }
        containsIgnoredSections = sortpomPiScanner.containsIgnoredSections();
    }

    public boolean existsIgnoredSections() {
        return containsIgnoredSections;
    }

    public String replaceIgnoredSections() {
        if (containsIgnoredSections) {
            return ignoredSectionsMap.replaceIgnoredSections(originalXml);
        }
        return originalXml;
    }

    public String revertIgnoredSections(String sortedXml) {
        if (containsIgnoredSections) {
            return ignoredSectionsMap.revertIgnoredSections(sortedXml);
        }
        return sortedXml;
    }
}
