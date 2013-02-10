package sortpom.configuration;

import com.intellij.openapi.command.impl.DummyProject;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;


/**
 * @author bjorn
 * @since 2013-01-02
 */
public class SortPomProjectConfigurationTest {

    //@Ignore("Live testing only")
    @Test
    public void testLive() throws Exception {
        SortPomProjectConfiguration configuration = new SortPomProjectConfiguration(DummyProject.getInstance());

        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //3. Create components and put them in the frame.
        frame.getContentPane().add(configuration.createComponent(), BorderLayout.CENTER);

        //4. Size the frame.
        frame.pack();

        //5. Show it.
        frame.setVisible(true);
        
        Thread.sleep(30*1000);
    }
}
