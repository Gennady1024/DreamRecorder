package com.dream.Filters;
import com.dream.Data.DataStream;
/**
 * Created with IntelliJ IDEA.
 * User: GENA
 * Date: 13.07.14
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
public class FilterTest_21  extends Filter<Integer> {
    private int derivative = 0;
    private int derivative_1 = 0;
    private int derivative_2 = 0;
    private final int NOISE_LEVEL = 400;
    private final int SPEED_MIN = 100;
    private int sum = 0;
    private int sum_final = 0;
    private int sum_index = 0;
    
    private boolean is_3_points_test = false;


    public FilterTest_21 (DataStream<Integer> inputData) {
        super(inputData);
    }

    

    @Override
    protected Integer getData(int index) {
        int result = 0;
        if(index > 2) {
            derivative = inputData.get(index) - inputData.get(index - 1);
            if((Math.abs(derivative) > SPEED_MIN) && (Math.signum(derivative) * Math.signum(sum) >= 0)){
                if(!is_3_points_test) {
                    sum += derivative;
                    sum_final = sum;
                    sum_index = index;
                }

            } 
            else if(sum != 0) {
                    is_3_points_test = true;
            }
        }

        if(is_3_points_test) {
            sum_final += derivative;
            if(index == sum_index + 3) {
                is_3_points_test = false;
                if( ((Math.signum(sum_final) * Math.signum(sum)) > 0 ) && (Math.abs(sum_final) - Math.abs(sum) >= 3*SPEED_MIN) ) {
                     sum=sum_final;
                     sum_index = index;                    
                }
                else {
                    if (Math.abs(sum) > NOISE_LEVEL) {
                        result =  sum;
                    }
                    sum = 0;
                    sum_final = 0;
                }
            }
        }    
        return result;
    }

    protected boolean isEqualSign(int a, int b) {
        if( ( a >= 0 )  && (b >= 0) ){
            return true;
        }

        if( ( a <= 0 )  && (b <= 0) ){
            return true;
        }

        return false;
    }

}
