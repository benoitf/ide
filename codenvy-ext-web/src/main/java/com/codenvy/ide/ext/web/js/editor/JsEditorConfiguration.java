/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.web.js.editor;

import com.codenvy.ide.MimeType;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.texteditor.api.AutoEditStrategy;
import com.codenvy.ide.texteditor.api.TextEditorConfiguration;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.texteditor.api.parser.BasicTokenFactory;
import com.codenvy.ide.texteditor.api.parser.CmParser;
import com.codenvy.ide.texteditor.api.parser.Parser;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The css css type editor configuration.
 *
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 */
public class JsEditorConfiguration extends TextEditorConfiguration {


    private Set<AutoEditStrategyFactory>      autoEditStrategyFactories;
    private DefaultCodeAssistProcessor defaultProcessor;

    /**
     * Build a new Configuration with the given set of strategies.
     * @param autoEditStrategyFactories the strategy factories
     */
    public JsEditorConfiguration(Set<AutoEditStrategyFactory> autoEditStrategyFactories,
                                 DefaultCodeAssistProcessor defaultProcessor) {
        this.autoEditStrategyFactories = autoEditStrategyFactories;
        this.defaultProcessor = defaultProcessor;
    }

    /** {@inheritDoc} */
    @Override
    public Parser getParser(@NotNull TextEditorPartView view) {
        CmParser parser = getParserForMime(MimeType.TEXT_JAVASCRIPT);
        parser.setNameAndFactory("javascript", new BasicTokenFactory());
        return parser;
    }



    @Override public StringMap<CodeAssistProcessor> getContentAssistantProcessors(@NotNull TextEditorPartView view) {
        if (defaultProcessor.getProcessors() == null || defaultProcessor.getProcessors().size() == 0) {
            return null;
        }
        StringMap<CodeAssistProcessor> map = Collections.createStringMap();
        map.put(Document.DEFAULT_CONTENT_TYPE, defaultProcessor);
        return map;
    }


    /**
     * Adds strategy for Interpolation brace completion
     * @param view the source viewer to be configured by this configuration
     * @param contentType the content type for which the strategies are applicable
     * @return
     */
    @Override
    public AutoEditStrategy[] getAutoEditStrategies(TextEditorPartView view, String contentType) {
        // Get super class strategy
        AutoEditStrategy[] parentStrategy = super.getAutoEditStrategies(view, contentType);

        // No injected strategies, go with default
        if (autoEditStrategyFactories == null || autoEditStrategyFactories.size() == 0) {
            return parentStrategy;
        }

        AutoEditStrategy[] strategies = new AutoEditStrategy[parentStrategy.length + autoEditStrategyFactories.size()];
        System.arraycopy(parentStrategy, 0, strategies, 0, parentStrategy.length);
        int i = parentStrategy.length;
        for (AutoEditStrategyFactory strategyFactory : autoEditStrategyFactories) {
            strategies[i++] = strategyFactory.build(view, contentType);
        }
        return strategies;
    }

}
