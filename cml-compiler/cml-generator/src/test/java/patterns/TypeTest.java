package patterns;

import cml.language.foundation.Type;
import generic.TemplateLangTest;
import org.junit.Test;

public class TypeTest extends TemplateLangTest
{
    public TypeTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getTemplatePath()
    {
        return "patterns/type";
    }

    @Test
    public void typeName()
    {
        for (String name: commonNames)
        {
            testTypeName(name);
        }
    }

    private void testTypeName(String name)
    {
        testTemplateWithNamedElement("typeName", Type.create(name, null), pascalCase(name));
    }
}