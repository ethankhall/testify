package io.ehdev.testify.dbtestbuilder

class InvalidDatabaseOperationException extends RuntimeException{

    InvalidDatabaseOperationException() {
    }

    InvalidDatabaseOperationException(Throwable throwable){
        super(throwable)
    }
}
