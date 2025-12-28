package musicplay.datastructure;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * 提供通用随机功能的工具类。
 */
public class RandomUtil {
	
	
    /**
     * 使用算法对列表进行洗牌,实现随机播放
     * 这个方法不修改原始列表，而是返回一个新创建的、被打乱顺序的列表。
     *
     * @param source 要进行洗牌的源列表。
     * @return 一个包含源列表所有元素但顺序随机的新列表。
     */
    public static List shuffle(List source) {
        List shuffledList = new ArrayList<>(source);
        
        // TODO 待实现
        
        return shuffledList;
    }
	

    /**
     * 创建一个静态的、唯一的Random实例。
     * 这样做可以避免在每次需要随机数时都创建一个新的Random对象，从而提高性能。
     */
    private static final Random random = new Random();

    /**
     * 获取一个在 [0, bound) 区间内的随机整数。
     *
     * @param bound 随机数的上界（不包含）。此值必须是正数。
     * @return 一个从 0 (含) 到 bound (不含) 的随机整数。
     * @throws IllegalArgumentException 如果 bound 不是正数。
     */
    public static int getNextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive.");
        }
        return random.nextInt(bound);
    }


} 