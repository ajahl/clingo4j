/*
 * Copyright 2017 andrej.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.clingo4j.api;

/**
 *
 * @author andrej
 */
public class Location {
    
    private final long beginColumn;
    
    private final String beginFile;
    
    private final long beginLine;
    
    private final long endColumn;
    
    private final String endFile;
    
    private final long endLine;

    public Location(long beginColumn, String beginFile, long beginLine, long endColumn, String endFile, long endLine) {
        this.beginColumn = beginColumn;
        this.beginFile = beginFile;
        this.beginLine = beginLine;
        this.endColumn = endColumn;
        this.endFile = endFile;
        this.endLine = endLine;
    }

    public long getBeginColumn() {
        return beginColumn;
    }

    public String getBeginFile() {
        return beginFile;
    }

    public long getBeginLine() {
        return beginLine;
    }

    public long getEndColumn() {
        return endColumn;
    }

    public String getEndFile() {
        return endFile;
    }

    public long getEndLine() {
        return endLine;
    }


    
}
