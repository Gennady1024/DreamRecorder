package com.dream.Filters;
import com.dream.Data.DataStream;
/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 13.07.14
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class FilterTest_2  extends Filter<Integer> {
    private int derivative_0 = 0;
    private int derivative_1 = 0;
    private int derivative_2 = 0;
    private final int NOISELEVEL = 0;
    private int sum_1 = 0;
    private int sum_2 = 0;

    public FilterTest_2 (DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        if(index > 2) {
            derivative_0 = inputData.get(index) - inputData.get(index - 1);
            if(Math.abs(derivative_0) > NOISELEVEL){
                derivative_1 = derivative_0;
                sum_1 += derivative_0;
            }

            sum_2 = 0;
        }

        if((derivative_1 < 0 && derivative_2 > 0) ||
           (derivative_1 > 0 && derivative_2 < 0))    {
            sum_2 = (sum_1);
            sum_1 = 0;
            derivative_1 = 0;
        }
        derivative_2 = derivative_1;
        return sum_2;
    }
}
