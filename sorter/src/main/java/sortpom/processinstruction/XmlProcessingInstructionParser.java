package sortpom.processinstruction;

import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;

/**
 * Handling for xml processing instructions in the pom file. Supports ignore and resume
 *
 * @author bjorn
 * @since 2013-12-28
 */
public class XmlProcessingInstructionParser {
    private final IgnoredSectionsStore ignoredSectionsStore = new IgnoredSectionsStore();
    private String originalXml;
    private SortPomLogger logger;
    private boolean containsIgnoredSections = false;

    public void setup(SortPomLogger logger) {
        this.logger = logger;
    }

    /** Checks if pom file contains any processing instructions */
    public void scanForIgnoredSections(String originalXml) {
        this.originalXml = originalXml;
        SortpomPiScanner sortpomPiScanner = new SortpomPiScanner(logger);
        sortpomPiScanner.scan(originalXml);
        if (sortpomPiScanner.isScanError()) {
            throw new FailureException(sortpomPiScanner.getFirstError());
        }
        containsIgnoredSections = sortpomPiScanner.containsIgnoredSections();
    }

    /** Returns true if sortpom processing instructions exists */
    public boolean existsIgnoredSections() {
        return containsIgnoredSections;
    }

    /** Stores all ignored sections and replaces each with a processing instruction token */
    public String replaceIgnoredSections() {
        if (containsIgnoredSections) {
            return ignoredSectionsStore.replaceIgnoredSections(originalXml);
        }
        return originalXml;
    }

    /** Reverts the processing instruction token back to original content */
    public String revertIgnoredSections(String sortedXml) {
        if (containsIgnoredSections) {
            return ignoredSectionsStore.revertIgnoredSections(sortedXml);
        }
        return sortedXml;
    }
}
