import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ImageWatermarkApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 获取图片文件路径
            System.out.print("请输入图片文件路径: ");
            String imagePath = scanner.nextLine();
            File imageFile = new File(imagePath);

            // 验证文件是否存在
            if (!imageFile.exists() || !imageFile.isFile()) {
                System.out.println("错误: 图片文件不存在或不是一个有效的文件!");
                return;
            }

            // 读取拍摄时间作为水印文本
            String watermarkText = ExifReader.getShootingDate(imageFile);
            System.out.println("将使用拍摄时间作为水印: " + watermarkText);

            // 获取字体大小
            System.out.print("请输入水印字体大小: ");
            int fontSize = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            // 选择颜色
            System.out.println("请选择水印颜色:");
            System.out.println("1. 黑色");
            System.out.println("2. 白色");
            System.out.println("3. 红色");
            System.out.println("4. 蓝色");
            System.out.println("5. 绿色");
            System.out.println("6. 黄色");
            System.out.print("请输入颜色编号: ");
            int colorChoice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            Color watermarkColor = getColorByChoice(colorChoice);

            // 选择位置
            System.out.println("请选择水印位置:");
            System.out.println("1. 左上角");
            System.out.println("2. 居中");
            System.out.println("3. 右下角");
            System.out.print("请输入位置编号: ");
            int position = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            // 创建输出目录
            File parentDir = imageFile.getParentFile();
            String outputDirName = parentDir.getName() + "_watermark";
            File outputDir = new File(parentDir, outputDirName);

            if (!outputDir.exists()) {
                if (!outputDir.mkdir()) {
                    System.out.println("错误: 无法创建输出目录!");
                    return;
                }
            }

            // 创建输出文件
            String fileName = imageFile.getName();
            String outputFileName = fileName.substring(0, fileName.lastIndexOf('.')) +
                    "_watermark" + fileName.substring(fileName.lastIndexOf('.'));
            File outputFile = new File(outputDir, outputFileName);

            // 添加水印
            ImageWatermarker.addWatermark(imageFile, outputFile, watermarkText,
                    fontSize, watermarkColor, position);

            System.out.println("水印添加成功! 输出文件路径: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("处理图片时出错: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("程序出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // 根据用户选择返回对应的颜色
    private static Color getColorByChoice(int choice) {
        switch (choice) {
            case 1: return Color.BLACK;
            case 2: return Color.WHITE;
            case 3: return Color.RED;
            case 4: return Color.BLUE;
            case 5: return Color.GREEN;
            case 6: return Color.YELLOW;
            default: return Color.BLACK; // 默认黑色
        }
    }
}
