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

    private int derivative_1 = 0;
    private int derivative_2 = 0;
    private final int SPEED_MIN = 100;
    private final int NOISE_LEVEL = 400;
    private int sum = 0;

    public FilterTest_2 (DataStream<Integer> inputData) {
        super(inputData);
    }

    @Override
    protected Integer getData(int index) {
        int result = 0;
        int derivative = 0;
        

        derivative = getDerivative(index);
        if(Math.abs(derivative) > SPEED_MIN){
            sum += derivative;
            derivative_1 = derivative;
        }

        if( isOppositeSign(derivative_1, derivative_2))    {
            if(sum > NOISE_LEVEL) {
                result = sum;
            }
            sum = 0;
            derivative_1 = 0;
        }
        derivative_2 = derivative_1;
        return result;
    }

    protected boolean isOppositeSign(int a, int b) {
        if((a < 0 && b > 0) || (a > 0 && b < 0)) {
            return true;
        }

        return false;
    }

    protected int getDerivative(int index) {
        int bufferSize =  0;
        if (index <= bufferSize) {
            return 0;
        }
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i <= bufferSize; i++) {
            sum1 = inputData.get(index+i);
            sum2 = inputData.get(index - 1 - i);
        }

        return (sum1 - sum2)/ (bufferSize + 1);
    }
}
