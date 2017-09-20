/*
 * Copyright (c) 2012-2017 The ANTLR Project. All rights reserved.
 * Use of this file is governed by the BSD 3-clause license that
 * can be found in the LICENSE.txt file in the project root.
 */
package org.antlr.v4.codegen.target;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.codegen.CodeGenerator;
import org.antlr.v4.codegen.Target;
import org.antlr.v4.codegen.UnicodeEscapes;
// import org.antlr.v4.tool.ErrorType;
import org.antlr.v4.tool.ast.GrammarAST;
// import org.stringtemplate.v4.NumberRenderer;
// import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
// import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;
// import org.stringtemplate.v4.misc.STMessage;

public class RustTarget extends Target {

	protected static final String[] rustKeywords = {
		"_",	"abstract",	"alignof",	"as",	"become",
		"box",	"break",	"const",	"continue",	"crate",
		"do",	"else",	"enum",	"extern",	"false",
		"final",	"fn",	"for",	"if",	"impl",
		"in",	"let",	"loop",	"macro",	"match",
		"mod",	"move",	"mut",	"offsetof",	"override",
		"priv",	"proc",	"pub",	"pure",	"ref",
		"return",	"Self",	"self",	"sizeof",	"static",
		"struct",	"super",	"trait",	"true",	"type",
		"typeof",	"unsafe",	"unsized",	"use",	"virtual",
		"where",	"while",	"yield"
	};

	/** Avoid grammar symbols in this set to prevent conflicts in gen'd code. */
	protected final Set<String> badWords = new HashSet<String>();

	public RustTarget(CodeGenerator gen) {
		super(gen, "Rust");
	}

    @Override
    public String getVersion() {
        return "4.7";
    }

	 // Used by org.antlr.v4.codegen.model.SerializedATN constructor only at present
	@Override
	public String encodeIntAsCharEscape(int v) {
		return Integer.toString(v);
	}

	public Set<String> getBadWords() {
		if (badWords.isEmpty()) {
			addBadWords();
		}

		return badWords;
	}

	protected void addBadWords() {
		badWords.addAll(Arrays.asList(rustKeywords));
		badWords.add("rule");
		badWords.add("parserRule");
	}

	@Override
	protected boolean visibleGrammarSymbolCausesIssueInGeneratedCode(GrammarAST idNode) {
		return getBadWords().contains(idNode.getText());
	}

	@Override
	protected STGroup loadTemplates() {
		STGroup result = super.loadTemplates();
		result.registerRenderer(String.class, new StringRenderer(), true);
		return result;
	}

	@Override
	protected void appendUnicodeEscapedCodePoint(int codePoint, StringBuilder sb) {
		// Rust and Swift share the same escaping style.
		UnicodeEscapes.appendSwiftStyleEscapedCodePoint(codePoint, sb);
	}
}
