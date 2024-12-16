# 抽象cloneVo是否是为了方便书写，而忽略了效率？
作者：厦门大学信息学院2019级本科生张浩山

本项目是需要实现**高并发、大负载的要求**。但是根据java的语言特性反射机制本身是非常慢的一种方法。而在`cloneVo`这个方法中大量使用了反射机制，来实现类型转换的需求。并且在大量需要高并发大负载的模块——例如商品模块中都有使用。

并且该机制并非是不可以替代的，可以使用构造方法来进行替代，并且速率会快得多，以下为测试代码。同时拷贝10000次一个`Payment`到`Vo`对象。第一次使用`cloneVo`，第二次使用手写构造方法来实现转换。为了避免被优化掉加了无意义的`StringBuilder`在生成对象后读取了对象的值。

并且为了避免StringBuilder本身的2的次方数扩容机制，影响测试，可以交换构造方法和cloneVo两个方法的顺序，来进行测试。经过多次测试cloneVo的速度都要明显慢于构造函数。该方法的使用可能严重影响系统效率。

```java
    @Test
    public void speedTest() {
        StringBuilder temp = new StringBuilder();
        PaymentDetailRetVo paymentDetailRetVo = new PaymentDetailRetVo();
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setDescr("测试");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            paymentDetailRetVo = cloneVo(payment, PaymentDetailRetVo.class);
            temp.append(paymentDetailRetVo.getDescr());
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("cloneVo耗时：%dms\n", (endTime - startTime));
        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            paymentDetailRetVo = new PaymentDetailRetVo(payment);
            temp.append(paymentDetailRetVo.getDescr());

        }
        long endTime2 = System.currentTimeMillis();
        System.out.printf("构造函数：%dms\n", (endTime2 - startTime2));
    }
```

以下为两次测试的截图。

![image](image3.png)

![image](image4.png)

*备注*：

- 参考资料：https://cloud.tencent.com/developer/article/1543621
- `Payment`类有十几个字段
- 构造方法直接采用`字段=Payment.get字段()`赋值的方式