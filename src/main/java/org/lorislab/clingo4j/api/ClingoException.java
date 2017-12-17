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
public class ClingoException extends RuntimeException {
    
    private static final long serialVersionUID = -5124979156179474046L;
    
    private final int clingoErrorCode;
    
    private final String clingoErrorMessage;

    public ClingoException() {
        super();
        this.clingoErrorCode = -1;
        this.clingoErrorMessage = null;
    }
    
    public ClingoException(int errorCode, String errorMessage, String message) {
        super(message);
        this.clingoErrorCode = errorCode;
        this.clingoErrorMessage = errorMessage;
    }

    public int getClingoErrorCode() {
        return clingoErrorCode;
    }

    public String getClingoErrorMessage() {
        return clingoErrorMessage;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Clingo error code: " + clingoErrorCode + " message: " + clingoErrorMessage;
    }
    
    
    
}