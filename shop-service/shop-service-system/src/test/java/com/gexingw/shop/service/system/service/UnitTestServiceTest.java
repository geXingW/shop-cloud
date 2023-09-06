package com.gexingw.shop.service.system.service;

import com.gexingw.shop.service.system.mapper.AdminMapper;
import com.gexingw.shop.service.system.vo.AdminAuthInfo;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 * shop-cloud.
 *
 * @author GeXingW
 * @date 2023/9/5 9:24
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({UnitTestService.class})
public class UnitTestServiceTest {

    @Mock
    AdminMapper adminMapper;

    @InjectMocks
    UnitTestService unitTestService;

    @Test
    @SneakyThrows
    public void testPrivateMethod() {
        Boolean result1 = Whitebox.invokeMethod(new UnitTestService(), "isEvenNumberPrivate", 2);
        Assertions.assertTrue(result1);

        Boolean result2 = Whitebox.invokeMethod(new UnitTestService(), "isEvenNumberPrivate", 1);
        Assertions.assertFalse(result2);

        UnitTestService unitTestService = PowerMockito.spy(new UnitTestService());
        PowerMockito.when(unitTestService, "privateMethod", Mockito.anyInt()).thenReturn(true);
        Boolean result3 = Whitebox.invokeMethod(new UnitTestService(), "isEvenNumberPrivate", 1);
        Assertions.assertFalse(result3);

    }

    @Test
    public void testPublicMethod() {
        UnitTestService unitTestService = PowerMockito.spy(new UnitTestService());

        Assertions.assertTrue(unitTestService.publicMethod(2));
        Assertions.assertFalse(unitTestService.publicMethod(1));

        Assertions.assertTrue(unitTestService.isEvenNumberPublic(2));
        Assertions.assertFalse(unitTestService.isEvenNumberPublic(1));

        PowerMockito.when(unitTestService.publicMethod(Mockito.anyInt())).thenReturn(true);
        Assertions.assertTrue(unitTestService.isEvenNumberPublic(1));
    }

    @Test
    public void testStaticMethod() {
        Assertions.assertTrue(UnitTestService.staticMethod(2));
        Assertions.assertFalse(UnitTestService.staticMethod(1));

        PowerMockito.spy(UnitTestService.class);
        PowerMockito.when(UnitTestService.staticMethod(Mockito.anyInt())).thenReturn(true);
        Assertions.assertTrue(UnitTestService.staticMethod(1));
        Assertions.assertTrue(UnitTestService.isEvenNumberStatic(1));

    }

    @Test
    public void testQueryDB() {
        AdminAuthInfo.Info info = new AdminAuthInfo.Info();
        info.setId(1L);

        PowerMockito.when(adminMapper.findByAuthUserId(Mockito.anyLong())).thenReturn(info);

        AdminAuthInfo.Info authInfo = unitTestService.queryDB();
        System.out.println(authInfo);
    }

}
