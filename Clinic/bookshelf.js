'use strict';

var knex=require('knex')(require('./knexfile')[process.env.NODE_ENV]),
    bookshelf=require('bookshelf')(knex);

bookshelf.plugin('registry');
module.exports=bookshelf;


