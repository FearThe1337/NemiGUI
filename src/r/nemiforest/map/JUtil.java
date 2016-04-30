package r.nemiforest.map;

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public interface JUtil {
    static void setFixedSize(Container component, Dimension dimension) {
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
    }


    FileFilter imageOpenFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if(!f.isFile())
                return f.isDirectory();

            String name = f.getName().toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") ||name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".bmp");
        }

        @Override
        public String getDescription() {
            return "Image files.";
        }
    };

    FileFilter imageSaveFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if(!f.isFile())
                return f.isDirectory();

            String name = f.getName().toLowerCase();
            return name.endsWith(".png");
        }

        @Override
        public String getDescription() {
            return "Image files.";
        }
    };
}
