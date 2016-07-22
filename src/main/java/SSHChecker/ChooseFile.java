package SSHChecker;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public interface ChooseFile {
    File OpenFileDialog(FileFilter fiter);
    File SaveFileDialog(int typeSave, FileFilter fiter);
}
