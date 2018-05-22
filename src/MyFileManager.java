import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyFileManager extends JFrame {
    private ListModel listModel = new DefaultListModel();
    private JList jList = new JList(listModel);
    private JLabel location = new JLabel();
    private ArrayList<FileItem> files = new ArrayList<FileItem>();
    private File directory = null;

    public MyFileManager() {
        super("File Manager");
        this.setBounds(10, 10, 500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane Scroll = new JScrollPane(jList);
        this.add(location, BorderLayout.PAGE_START);
        this.add(Scroll, BorderLayout.CENTER);
        location.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jList.addMouseListener(mouseListener);
        location.addMouseListener(lableListener);
    }

    private void PrintDirectory(File f) {
        if (f == null) return;
        if (!f.isDirectory()) {
            if (f.isFile()) {
                try {
                    Desktop.getDesktop().open(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        File[] lists = f.listFiles();
        if (lists == null) {
            System.out.println("NO PERMISSION TO OPEN FOLDER " + f);
            return;
        }
        directory = f;
        location.setText(f.getAbsolutePath());

        files.clear();

        for (File item : lists) {
            files.add(new FileItem(item));
        }
        files.sort(FileItem::compareTo);

        //jList.setListData(files.toArray());
    }

    public void goTo(File file) {
        PrintDirectory(file);
    }

    public static class FileItem extends JLabel {
        public File getFile() {
            return file;
        }

        private final File file;

        public FileItem(File file) {
            this.file = file;
            this.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
        }

        public int compareTo(FileItem fileItem) {
            if (file.isDirectory() == fileItem.file.isDirectory()) {
                return file.getName().compareToIgnoreCase(fileItem.file.getName());
            }
            return file.isDirectory() ? -1 : 1;
        }

        @Override
        public String toString() {
            return file.isDirectory() ? ("[" + file.getName() + "]") : file.getName();
        }
    }

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            JList<String> theList = (JList) e.getSource();
            if (e.getClickCount() == 2) {
                int index = theList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    goTo(files.get(index).getFile());
                }
            }
        }
    };

    MouseListener lableListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (directory != null)
                    goTo(directory.getParentFile());
            }
        }
    };
}