package com.mulesoft.documentation.builder.linkmanager;

import org.junit.Test;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/21/15.
 */
public class ClientTest {

    @Test
    public void worksWithValidParams() {
        String source = "-s/Users/sean.osterberg/docs-source/mulesoft-docs/";
        String dest = "-d/tmp";
        Client.main(new String[] { source, dest });
    }
}
