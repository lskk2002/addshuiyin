import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageWatermarker {
    // 水印位置常量
    public static final int POSITION_TOP_LEFT = 1;
    public static final int POSITION_CENTER = 2;
    public static final int POSITION_BOTTOM_RIGHT = 3;

    /**
     * 给图片添加水印
     * @param sourceImage 源图片文件
     * @param destImage 目标图片文件
     * @param text 水印文本
     * @param fontSize 字体大小
     * @param color 字体颜色
     * @param position 水印位置
     */
    public static void addWatermark(File sourceImage, File destImage, String text,
                                    int fontSize, Color color, int position) throws IOException {
        // 读取原始图片
        BufferedImage image = ImageIO.read(sourceImage);

        // 创建一个可绘制的图像副本
        BufferedImage watermarkedImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 获取绘图上下文
        Graphics2D g = watermarkedImage.createGraphics();

        // 绘制原始图像
        g.drawImage(image, 0, 0, null);

        // 设置水印字体和颜色
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(font);
        g.setColor(color);

        // 计算水印位置
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        int x = 0, y = 0;

        switch (position) {
            case POSITION_TOP_LEFT:
                x = 10;
                y = textHeight + 10;
                break;
            case POSITION_CENTER:
                x = (image.getWidth() - textWidth) / 2;
                y = (image.getHeight() + textHeight) / 2;
                break;
            case POSITION_BOTTOM_RIGHT:
                x = image.getWidth() - textWidth - 10;
                y = image.getHeight() - 10;
                break;
            default:
                x = image.getWidth() - textWidth - 10;
                y = image.getHeight() - 10;
        }

        // 添加水印
        g.drawString(text, x, y);

        // 释放资源
        g.dispose();

        // 获取文件格式
        String format = sourceImage.getName().substring(sourceImage.getName().lastIndexOf('.') + 1);
        if (format.equalsIgnoreCase("jpg")) format = "jpeg";

        // 保存水印图片
        ImageIO.write(watermarkedImage, format, destImage);
    }
}
