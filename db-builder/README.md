#DB Builder
This section of testify is about populating your tests fixtures in a way that is easy to create relationships. If you have used DBUnit before, the XML makes it rather difficult (or impossible) to create database tests that involve relationships, and have them execute in parallel. This is because you are required to specify the primary key of the tables so that you can properly link the entries together.

This is where DB Builder comes in handy. While the project is still young, it should be able to do basic test fixture setups.

For example:

    DBTestCase {
        def table1Ids = [
                table1( field1: 1, field2: 2, field3: 3),
                table1( field1: 4, field2: 5, field3: 6),
                table1( field1: 7, field2: 8, field3: 9)
                ]

        table2( field1: 1, field2: 2, field3: 3, refId: table1Ids[0])
        table2( field1: 4, field2: 5, field3: 6, refId: table1Ids[1])
        table2( field1: 7, field2: 8, field3: 9, refId: table1Ids[2])
    }

This is a test that is used in the unit tests for DB Builder. The syntax is pretty easy. First you see the `DBTestCase`. This lets the fixture builder know that this will be your test that needs to be loaded. There can (eventually) be multiples of these in a test case file (can be named what ever you want. I use `.db` so they are easy to find). After that you see there there is a `def table1Ids`, this creates a variable called `table1Ids`(that will be a list), and saves the results of `table1(...)` into it. Here is where the magic happens. Let's take a look at it more carefully.

###Describing an entry

    table1( field1: 1, field2: 2, field3: 3)

This is the heart of this project. This line will translate to 

    insert into table1 (field1, filed2, field3) values (1, 2, 3)

In the code we use named parameters, but you get the gest of it. `table1` returns the primary key that was created by the insert. This allows us to store the keys in a list and use them as we do in the first example

### Everything is Groovy
The file that I used to describe this is Groovy (with some hand waving done for you). So this will allow you do write methods and code to easily generate the fixtures you need for your project. No longer will you have to use aquard syntax to describe simple things!

### Another example

    DBTestCase("this is an example with a description") {
        table2( field1: 1, field2: 2, field3: 3, refId: 
            table1( field1: 1, field2: 2, field3: 3)
        )
    }

In this example we have a description of the test case and using some more functional syntax we create a row in `table2` after we insert and get the primary key back from `table1`.

#Coming soon

How to use it in your projects :-D
