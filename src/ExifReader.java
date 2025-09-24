import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ExifReader {
    // 拍摄时间标签
    private static final String EXIF_DATE_TAG = "DateTimeOriginal";

    /**
     * 从图片文件中读取拍摄时间111
     * @param imageFile 图片文件
     * @return 拍摄时间的年月日字符串，格式为"yyyy-MM-dd"
     */
    public static String getShootingDate(File imageFile) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(imageFile)) {
            // 获取图片读取器
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext()) {
                return getCurrentDate(); // 没有可用的读取器，返回当前日期
            }

            ImageReader reader = readers.next();
            reader.setInput(iis, true);

            // 获取元数据
            IIOMetadata metadata = reader.getImageMetadata(0);
            String[] names = metadata.getMetadataFormatNames();

            // 查找EXIF信息中的拍摄时间
            for (String name : names) {
                Node root = metadata.getAsTree(name);
                String date = parseNode(root);
                if (date != null) {
                    return formatDate(date);
                }
            }
        } catch (IOException e) {
            System.out.println("读取图片元数据时出错: " + e.getMessage());
        }

        // 如果无法读取EXIF信息，返回当前日期
        return getCurrentDate();
    }

    // 解析XML节点查找拍摄时间
    private static String parseNode(Node node) {
        if (node == null) return null;

        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            Node dateNode = attributes.getNamedItem(EXIF_DATE_TAG);
            if (dateNode != null) {
                return dateNode.getNodeValue();
            }
        }

        // 递归查找子节点
        Node child = node.getFirstChild();
        while (child != null) {
            String result = parseNode(child);
            if (result != null) {
                return result;
            }
            child = child.getNextSibling();
        }

        return null;
    }

    // 格式化日期为"yyyy-MM-dd"格式
    private static String formatDate(String exifDate) {
        try {
            // EXIF日期格式通常为"yyyy:MM:dd HH:mm:ss"
            SimpleDateFormat exifFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
            Date date = exifFormat.parse(exifDate);

            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            return targetFormat.format(date);
        } catch (ParseException e) {
            System.out.println("解析日期时出错: " + e.getMessage());
            return getCurrentDate();
        }
    }

    // 获取当前日期，格式为"yyyy-MM-dd"
    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
