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
package com.codenvy.ide.ext.java.worker;

import com.codenvy.ide.collections.Jso;
import com.codenvy.ide.ext.java.jdt.core.compiler.CharOperation;
import com.codenvy.ide.ext.java.jdt.core.search.IJavaSearchConstants;
import com.codenvy.ide.ext.java.jdt.internal.codeassist.ISearchRequestor;
import com.codenvy.ide.ext.java.jdt.internal.compiler.env.AccessRestriction;
import com.codenvy.ide.ext.java.jdt.internal.compiler.env.INameEnvironment;
import com.codenvy.ide.ext.java.jdt.internal.compiler.env.NameEnvironmentAnswer;
import com.codenvy.ide.ext.java.shared.JavaType;
import com.codenvy.ide.ext.java.worker.env.BinaryType;
import com.codenvy.ide.ext.java.worker.env.json.BinaryTypeJso;
import com.google.gwt.core.client.JavaScriptObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link com.codenvy.ide.ext.java.jdt.internal.compiler.env.INameEnvironment} interface, use JavaCodeAssistantService
 * for receiving data and SessionStorage for
 * cache Java type data in browser
 *
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version ${Id}: Jan 13, 2012 3:10:43 PM evgen $
 */
public class WorkerNameEnvironment implements INameEnvironment {

    private static Set<String> packages = new HashSet<String>();
    protected String restServiceContext;
    private   String projectPath;

    /**
     *
     */
    public WorkerNameEnvironment(String restContext, String wsName) {
        restServiceContext = restContext + "/java-name-environment" + wsName;
    }

    private static String convertSearchFilterToModelFilter(int searchFilter) {
        switch (searchFilter) {
            case IJavaSearchConstants.CLASS:
                return JavaType.CLASS.name();
            case IJavaSearchConstants.INTERFACE:
                return JavaType.INTERFACE.name();
            case IJavaSearchConstants.ENUM:
                return JavaType.ENUM.name();
            case IJavaSearchConstants.ANNOTATION_TYPE:
                return JavaType.ANNOTATION.name();
            // TODO
            // case IJavaSearchConstants.CLASS_AND_ENUM:
            // return NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_ENUMS;
            // case IJavaSearchConstants.CLASS_AND_INTERFACE:
            // return NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES;
            default:
                return null;
        }
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    /** {@inheritDoc} */
    @Override
    public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
        StringBuilder b = new StringBuilder();
        for (char[] c : compoundTypeName) {
            b.append(c).append('.');
        }
        b.deleteCharAt(b.length() - 1);

        final String key = validateFqn(b);
        if (WorkerTypeInfoStorage.get().containsKey(key)) {
            return new NameEnvironmentAnswer(WorkerTypeInfoStorage.get().getType(key), null);
        }

        if (projectPath != null) {
            if (packages.contains(key)) {
                return null;
            }
            StringBuilder builder = new StringBuilder();
            for (char[] chars : compoundTypeName) {
                builder.append(chars).append(',');
            }
            if (builder.length() > 1) builder.deleteCharAt(builder.length() - 1);
            String url =
                    restServiceContext + "/findTypeCompound?compoundTypeName=" + builder.toString() + "&projectpath=" +
                    projectPath;
            String result = runSyncReques(url);
            if (result != null) {
                Jso jso = Jso.deserialize(result);
                BinaryType type = new BinaryType(jso.<BinaryTypeJso>cast());
                WorkerTypeInfoStorage.get().putType(key, type);

                return new NameEnvironmentAnswer(type, null);
            } else return null;
//
//            return loadTypeInfo(key, projectPath);
        }
        return null;
    }

    private String validateFqn(StringBuilder builder) {
        if (builder.indexOf("<") != -1) {
            builder.setLength(builder.indexOf("<"));
        }
        return builder.toString();
    }

//    /**
//     * Load and store in TypeInfoStorage type info
//     *
//     * @param fqn
//     *         of the type
//     * @param projectPath
//     *         project
//     */
//    public NameEnvironmentAnswer loadTypeInfo(final String fqn, String projectPath) {
//        if (packages.contains(fqn)) {
//            return null;
//        }
//        String url =
//                restServiceContext + "/class-description?fqn=" + fqn + "&projectid=" + projectPath +
//                "&vfsid=" + vfsId;
//        String result = runSyncReques(url);
//        if (result != null) {
//
//
//            Jso jso = Jso.deserialize(result);
//            BinaryTypeImpl type = new BinaryTypeImpl(jso);
//            WorkerTypeInfoStorage.get().putType(fqn, type);
//
//            return new NameEnvironmentAnswer(type, null);
//        } else return null;
//    }

    /** {@inheritDoc} */
    @Override
    public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
        if ("package-info".equals(new String(typeName))) {
            return null;
        }

        StringBuilder b = new StringBuilder();
        for (char[] c : packageName) {
            b.append(c).append('.');
        }
        b.append(typeName);
        final String key = validateFqn(b);
        //TODO
//      if (TypeInfoStorage.get().getPackages(projectPath).contains(key))
//         return null;
        if (WorkerTypeInfoStorage.get().containsKey(key)) {
            return new NameEnvironmentAnswer(WorkerTypeInfoStorage.get().getType(key),
                                             null);
        }
        if (projectPath != null) {
            if (packages.contains(key)) {
                return null;
            }
            StringBuilder builder = new StringBuilder();
            for (char[] chars : packageName) {
                builder.append(chars).append(',');
            }
            if (builder.length() > 1) builder.deleteCharAt(builder.length() - 1);

            String url =
                    restServiceContext + "/findType?packagename=" + builder.toString() + "&typename=" + new String(typeName) +
                    "&projectpath=" + projectPath;
            String result = runSyncReques(url);
            if (result != null) {
                Jso jso = Jso.deserialize(result);
                BinaryType type = new BinaryType(jso.<BinaryTypeJso>cast());
                WorkerTypeInfoStorage.get().putType(key, type);

                return new NameEnvironmentAnswer(type, null);
            } else return null;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPackage(char[][] parentPackageName, char[] packageName) {
        //TODO maybe need more actions on this
        StringBuilder p = new StringBuilder();
        try {
            if (parentPackageName != null) {
                for (char[] seg : parentPackageName) {
                    p.append(seg).append('.');
                }
            }
            p.append(packageName);
            if (packages.contains(p.toString())) {
                return true;
            }
            StringBuilder builder = new StringBuilder();
            if (parentPackageName != null) {
                for (char[] chars : parentPackageName) {
                    builder.append(chars).append(',');
                }
                if (builder.length() > 1) builder.deleteCharAt(builder.length() - 1);
            }
            String url =
                    restServiceContext + "/package" + "?packagename=" + new String(packageName) + "&parent=" +
                    builder.toString()
                    + "&projectpath=" + projectPath;
            String findPackage = runSyncReques(url);
            boolean exist = findPackage != null && Boolean.parseBoolean(findPackage);
            if(exist) {
                packages.add(p.toString());
            }
            return exist;
//            if (findPackage != null) {
//                JSONArray jsonArray = JSONParser.parseLenient(findPackage).isArray();
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    packages.add(jsonArray.get(i).isString().stringValue());
//                }
//                return jsonArray.size() > 0;
//            }
//            return false;

        } catch (Exception e) {
            e.printStackTrace();
            //TODO log errors
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void cleanup() {
    }

    /**
     * Must be used only by CompletionEngine. The progress monitor is used to be able to cancel completion operations
     * <p/>
     * Find constructor declarations that are defined in the current environment and whose name starts with the given prefix. The
     * prefix is a qualified name separated by periods or a simple name (ex. java.util.V or V).
     * <p/>
     * The constructors found are passed to one of the following methods: ISearchRequestor.acceptConstructor(...)
     */
    @Override
    public void findConstructorDeclarations(char[] prefix, boolean camelCaseMatch, final ISearchRequestor requestor) {
//        int lastDotIndex = CharOperation.lastIndexOf('.', prefix);
//        char[] qualification, simpleName;
//        if (lastDotIndex < 0) {
//            qualification = null;
//            if (camelCaseMatch) {
//                simpleName = prefix;
//            } else {
//                simpleName = CharOperation.toLowerCase(prefix);
//            }
//        } else {
//            qualification = CharOperation.subarray(prefix, 0, lastDotIndex);
//            if (camelCaseMatch) {
//                simpleName = CharOperation.subarray(prefix, lastDotIndex + 1, prefix.length);
//            } else {
//                simpleName = CharOperation.toLowerCase(CharOperation.subarray(prefix, lastDotIndex + 1, prefix.length));
//            }
//        }
//        String url =
//                restServiceContext + "/classes-by-prefix" + "?prefix=" + new String(simpleName)
//                + "&projectid=" + projectPath + "&vfsid=" + vfsId;
//        try {
//            List<IBinaryType> typesByNamePrefix =
//                    WorkerTypeInfoStorage.get().getTypesByNamePrefix(new String(prefix), qualification != null);
//            for (IBinaryType object : typesByNamePrefix) {
//                addConstructor((BinaryTypeImpl)object, requestor);
//            }
//            String typesJson = runSyncReques(url);
//            JSONArray typesFromServer = null;
//            if (typesJson != null) {
//                typesFromServer = JSONParser.parseLenient(typesJson).isArray();
//                for (int i = 0; i < typesFromServer.size(); i++) {
//                    JSONObject object = typesFromServer.get(i).isObject();
//                    BinaryTypeImpl type = new BinaryTypeImpl(object.getJavaScriptObject().<Jso>cast());
//                    if (WorkerTypeInfoStorage.get().containsKey(String.valueOf(type.getFqn()))) {
//                        continue;
//                    }
//                    WorkerTypeInfoStorage.get().putType(new String(type.getFqn()), type);
//                    addConstructor(type, requestor);
//                }
//            }
//
//        } catch (Exception e) {
////            Log.error(getClass(), e);
//            //TODO log error
//        }
    }

//    private void addConstructor(BinaryTypeImpl type, final ISearchRequestor requestor) {
//        IBinaryMethod[] methods = type.getMethods();
//        boolean hasConstructor = false;
//        if (methods != null) {
//            for (IBinaryMethod method : methods) {
//                if (!method.isConstructor()) {
//                    continue;
//                }
//                int parameterCount = Signature.getParameterCount(method.getMethodDescriptor());
//                char[][] parameterTypes = Signature.getParameterTypes(method.getMethodDescriptor());
//                requestor.acceptConstructor(method.getModifiers(), type.getSourceName(), parameterCount,
//                                            method.getMethodDescriptor(), parameterTypes, method.getArgumentNames(), type.getModifiers(),
//                                            Signature.getQualifier(type.getFqn()), 0, new String(type.getSourceName()), null);
//                hasConstructor = true;
//            }
//        }
//        if (!hasConstructor) {
//            requestor.acceptConstructor(Flags.AccPublic, type.getSourceName(), -1,
//                                        null, // signature is not used for source type
//                                        CharOperation.NO_CHAR_CHAR, CharOperation.NO_CHAR_CHAR, type.getModifiers(),
//                                        Signature.getQualifier(type.getFqn()), 0, new String(type.getSourceName()), null);
//        }
//
//    }

    /**
     * Find the packages that start with the given prefix. A valid prefix is a qualified name separated by periods (ex. java.util).
     * The packages found are passed to: ISearchRequestor.acceptPackage(char[][] packageName)
     */
    @Override
    public void findPackages(char[] qualifiedName, final ISearchRequestor requestor) {
        final String pack = new String(qualifiedName);
        for (String key : packages) {
            if (key.startsWith(pack)) {
                requestor.acceptPackage(key.toCharArray());
            }
        }

    }

    private String runSyncReques(String url) {
        XmlHttpWraper xmlhttp = nativeRunSyncReques(url);
        int status = xmlhttp.getStatusCode();
        if (status == 200) {
            return xmlhttp.getResponseText();
        } else {
//            String message = null;
//            if (status == 204) {
//                message = "no content";
//            } else {
//                message = xmlhttp.getResponseText();
//            }
            //throw new RuntimeException("Server return " + message);
            // server not find info
            return null;
        }
    }

    private native XmlHttpWraper nativeRunSyncReques(String url)/*-{
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("GET", url, false);
        xmlhttp.send();
        return xmlhttp;
    }-*/;

    /**
     * Must be used only by CompletionEngine. The progress monitor is used to be able to cancel completion operations
     * <p/>
     * Find the top-level types that are defined in the current environment and whose name starts with the given prefix. The prefix
     * is a qualified name separated by periods or a simple name (ex. java.util.V or V).
     * <p/>
     * The types found are passed to one of the following methods (if additional information is known about the types):
     * ISearchRequestor.acceptType(char[][] packageName, char[] typeName) ISearchRequestor.acceptClass(char[][] packageName, char[]
     * typeName, int modifiers) ISearchRequestor.acceptInterface(char[][] packageName, char[] typeName, int modifiers)
     * <p/>
     * This method can not be used to find member types... member types are found relative to their enclosing type.
     */
    @Override
    public void findTypes(char[] qualifiedName, boolean b, boolean camelCaseMatch, int searchFor,
                          final ISearchRequestor requestor) {
        if (qualifiedName.length == 0) {
            return;
        }
//        String searchType = convertSearchFilterToModelFilter(searchFor);
//        String url = null;
//        if (searchType == null) {
//            int lastDotIndex = CharOperation.lastIndexOf('.', qualifiedName);
//            String typeSearch;
//            if (lastDotIndex < 0) {
//                typeSearch = "className";
//            } else {
//                typeSearch = "fqn";
//            }
//            url =
//                    restServiceContext + "/find-by-prefix/" + new String(qualifiedName) + "?where="
//                    + typeSearch + "&projectid=" + projectPath ;
//        } else {
//            url =
//                    restServiceContext + "/find-by-type/" + searchType + "?prefix="
//                    + new String(qualifiedName) + "&projectid=" + projectPath;
//        }
//        try {
//
//            String typesJson = runSyncReques(url);
//            if (typesJson != null) {
//
////                TypesListImpl typesList = TypesListImpl.deserialize(typesJson);
//
//
////                for (ShortTypeInfo info : typesList.getTypes()) {
////                    requestor.acceptType(info.getName().substring(0, info.getName().lastIndexOf(".")).toCharArray(),
////                                         info.getName().substring(info.getName().lastIndexOf(".") + 1).toCharArray(),
////                                         null,
////                                         info.getModifiers(),
////                                         null);
////                }
//            }
//            WorkerTypeInfoStorage.get().setShortTypesInfo(typesJson);
//        } catch (Throwable e) {
////            Log.error(getClass(), e);
//            throw new RuntimeException(e);
//            //TODO log error
//        }
    }

    /**
     * Find the top-level types that are defined
     * in the current environment and whose simple name matches the given name.
     * <p/>
     * The types found are passed to one of the following methods (if additional
     * information is known about the types):
     * ISearchRequestor.acceptType(char[][] packageName, char[] typeName)
     * ISearchRequestor.acceptClass(char[][] packageName, char[] typeName, int modifiers)
     * ISearchRequestor.acceptInterface(char[][] packageName, char[] typeName, int modifiers)
     * <p/>
     * This method can not be used to find member types... member
     * types are found relative to their enclosing type.
     */
    @Override
    public void findExactTypes(final char[] missingSimpleName, boolean b, int type, final ISearchRequestor storage) {
        findTypes(missingSimpleName, b, false, type, new ISearchRequestor() {

            @Override
            public void acceptType(char[] packageName, char[] typeName, char[][] enclosingTypeNames, int modifiers,
                                   AccessRestriction accessRestriction) {
                if (CharOperation.equals(missingSimpleName, typeName)) {
                    storage.acceptType(packageName, typeName, enclosingTypeNames, modifiers, accessRestriction);
                }
            }

            @Override
            public void acceptPackage(char[] packageName) {
            }

            @Override
            public void acceptConstructor(int modifiers, char[] simpleTypeName, int parameterCount, char[] signature,
                                          char[][] parameterTypes, char[][] parameterNames, int typeModifiers, char[] packageName,
                                          int extraFlags,
                                          String path, AccessRestriction access) {
            }
        });
    }

    private static final class XmlHttpWraper extends JavaScriptObject {
        /**
         *
         */
        protected XmlHttpWraper() {
        }

        public native int getStatusCode()/*-{
            return this.status;
        }-*/;

        public native String getResponseText()/*-{
            return this.responseText;
        }-*/;

    }
}
