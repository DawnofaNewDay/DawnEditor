package net.pokefriends.DawnEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import com.formdev.flatlaf.*;

public class DawnEditor extends JFrame {

    /* -- FRAME & TEXTBOX -- */
    String windowTitle = "Dawn's Text Editor";
    JScrollPane pane;
    JTextArea text;

    /* -- MENUBAR -- */
    JMenuBar menuBar;

    // File
    JMenu file;
    Path filePath;
    JMenuItem open;
    JMenuItem save;
    JMenuItem saveAs;

    // Options
    JMenu options;
    JMenuItem wordWrap;

    DawnEditor(String argPath) {

        /* -- FRAME & TEXTBOX -- */
        setTitle(windowTitle);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        text = new JTextArea();
        if (argPath != null) {
            try {
                filePath = Path.of(argPath);
                text.setText(Files.readString(filePath));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        text.setLineWrap(true);
        text.setBorder(BorderFactory.createCompoundBorder(text.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        /* -- MENUBAR -- */
        menuBar = new JMenuBar();
        file = new JMenu("File");
        options = new JMenu("Options");

        menuBar.add(file);
        menuBar.add(options);

        // File Menu
        open = new JMenuItem("Open...");
        open.addActionListener(openEvent);
        file.add(open);

        save = new JMenuItem("Save");
        save.addActionListener(saveEvent);
        file.add(save);

        saveAs = new JMenuItem("Save As...");
        saveAs.addActionListener(saveAsEvent);
        file.add(saveAs);

        // Options Menu
        wordWrap = new JCheckBoxMenuItem("Wrap Words");
        wordWrap.addActionListener(e -> text.setLineWrap(((AbstractButton) e.getSource()).getModel().isSelected()));

        options.add(wordWrap);

        /* -- IT'S GO TIME! -- */
        pane = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setBounds(5, 5, 5, 5);
        add(pane);
        setJMenuBar(menuBar);
        setVisible(true);
    }

    /* -- MENUBAR EVENTS -- */

    ActionListener openEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int returnval = fc.showOpenDialog(null);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                filePath = fc.getSelectedFile().toPath();
                try {
                    text.setText(Files.readString(filePath));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    };

    ActionListener saveEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (filePath == null) saveAsEvent.actionPerformed(e);
            try {
                PrintWriter out = new PrintWriter(filePath.toString());
                out.print(text.getText());
                out.close();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    ActionListener saveAsEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int returnval = fc.showSaveDialog(null);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                filePath = fc.getSelectedFile().toPath();
                try {
                    PrintWriter out = new PrintWriter(filePath.toString());
                    out.print(text.getText());
                    out.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    };

    /* -- MAIN -- */
    public static void main(String[] args) {
        /* -- LOOK & FEEL -- */
        System.setProperty("apple.awt.application.appearance", "system");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to change look and feel!");
            System.exit(-1);
        }

        /* -- FILE LOADING -- */
        System.out.println(Arrays.toString(args));
        String path;
        if (args.length > 0)
            path = args[0];
        else
            path = null;

        /* -- BEGIN -- */
        new DawnEditor(path);
    }
}