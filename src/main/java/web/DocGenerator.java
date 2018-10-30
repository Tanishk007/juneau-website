package web;
// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************

import static web.Console.*;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for generating the overview.html page.
 */
public class DocGenerator {

	static List<String> WARNINGS = new ArrayList<>();

	public static final String
		JUNEAU_VERSION = "7.2.1",
		JUNEAU_VERSION_NEXT = "7.2.2";


	/**
	 * Entry point.
	 *
	 * @param args Not used
	 */
	public static void main(String[] args) {
		System.setProperty("juneauVersion", JUNEAU_VERSION);
		System.setProperty("juneauVersionNext", JUNEAU_VERSION_NEXT);
		if (args.length == 0 || args.length == 1 && args[0].equals("build"))
			build();
	}


	private static void build() {
		try {
			long startTime = System.currentTimeMillis();

			File f = new File("src/main/resources/content");
			for (File fc : f.listFiles()) {
				String s = IOUtils.read(fc);
				StringBuffer sb = new StringBuffer();
				Pattern p = Pattern.compile("\\{\\@property ([^\\}]+)\\}");
				Matcher m = p.matcher(s);
				while (m.find())
					m.appendReplacement(sb, (String)System.getProperty(m.group(1)));
				m.appendTail(sb);
				IOUtils.writeFile("content/" + fc.getName(), sb.toString());
			}

			info("Generated content in {0}ms", System.currentTimeMillis()-startTime);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}