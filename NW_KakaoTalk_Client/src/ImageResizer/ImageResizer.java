package ImageResizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImageResizer {
    public static void LoginImage(JLabel lbl) {
        ImageIcon icon = (ImageIcon)lbl.getIcon();
        Image image = icon.getImage();

        Image newImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImage);

        lbl.setIcon(newIcon);
    } // 로그인창 이미지 크기 설정

    public static void MainBoardImage(JLabel lbl) {
        ImageIcon icon = (ImageIcon)lbl.getIcon();
        Image image = icon.getImage();

        Image newImage = image.getScaledInstance(102, 50, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImage);

        lbl.setIcon(newIcon);
    }

    public static void InterfaceImage(JButton[] btnGroup) {
        for (JButton btn : btnGroup) {
            ImageIcon btnIcon = (ImageIcon)btn.getIcon();
            Image btnImage = btnIcon.getImage();

            Image newImage = btnImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newImage);

            btn.setIcon(newIcon);
        }
    } // 인터페이스 이미지 크기 설정

    public static void FriendBoardImage(JButton[] btnGroup) {
        for (JButton btn : btnGroup) {
            ImageIcon btnIcon = (ImageIcon)btn.getIcon();
            Image btnImage = btnIcon.getImage();

            Image newImage = btnImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon newIcon = new ImageIcon(newImage);

            btn.setIcon(newIcon);
        }
    } // FriendBoard 이미지 크기 조정

    public static void UserBoardSetting(JLabel lblSetting) {
        ImageIcon icon = (ImageIcon)lblSetting.getIcon();
        Image image = icon.getImage();

        Image newImage = image.getScaledInstance(25,25, Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImage);

        lblSetting.setIcon(newIcon);
    }
}
