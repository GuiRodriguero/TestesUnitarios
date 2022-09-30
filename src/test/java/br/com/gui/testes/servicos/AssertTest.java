package br.com.gui.testes.servicos;

import br.com.gui.testes.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {

    @Test
    public void test(){
        Assert.assertTrue(true);
        Assert.assertFalse(false);

        Assert.assertEquals(1, 1);
        Assert.assertEquals(1, 2, 3);

        int i = 5;
        Integer i2 = 5;
        Assert.assertEquals(Integer.valueOf(i), i2);
        Assert.assertEquals(i, i2.intValue());

        Assert.assertEquals("Teste", "Teste");
        Assert.assertTrue("Teste".equalsIgnoreCase("teste"));

        Usuario u1 = new Usuario("Usuario 1");
        Usuario u2 = new Usuario("Usuario 1");
        Usuario u3 = null;
        Assert.assertEquals(u1, u2);
        Assert.assertSame(u1, u1);
        Assert.assertNull(u3);
    }
}
