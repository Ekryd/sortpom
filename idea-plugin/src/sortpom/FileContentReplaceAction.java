package sortpom;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

/**
 * Replaces the context of a file when the run method is invoked
 *
 * @author bjorn
 * @since 2013-01-02
 */
public class FileContentReplaceAction implements Runnable {
    private final String mySortedXml;
    private final PsiFile myPsiFile;

    public FileContentReplaceAction(String sortedXml, PsiFile psiFile) {
        this.mySortedXml = sortedXml;
        this.myPsiFile = psiFile;
    }

    public void run() {
        PsiDocumentManager documentManager = PsiDocumentManager.getInstance(myPsiFile.getProject());
        Document document = documentManager.getDocument(myPsiFile);
        document.setText(mySortedXml);

        documentManager.commitDocument(document);
    }
}
