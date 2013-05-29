define('plugin/branchlist/branch-table', [
    'jquery',
    'underscore',
    'widget/paged-table',
    'util/navbuilder',
    'model/page-state'
], function(
    $,
    _,
    PagedTable,
    navbuilder,
    pageState
) {
	
	 function BranchTable(branchTableSelector, currentBranchUrl, options) {
         var defaults = {
             target: branchTableSelector,
             ajaxDataType: 'json',
             tableMessageClass: 'branch-table-message',
             allFetchedMessageHtml: '<p class="no-more-results">' + 'No more branches' + '</p>',
             noneFoundMessageHtml: '<h3 class="no-results entity-empty">' + 'There are no branches' + '</h3>'
         };

         PagedTable.call(this, _.extend({}, defaults, options));

         this.currentBranchUrl = currentBranchUrl;
     }

     _.extend(BranchTable.prototype, PagedTable.prototype);

     BranchTable.prototype.buildUrl = function(start, limit) {
    	 console.log("Woo, URL with start and limit of: "+start + ", "+limit);
    	 var url = this.currentBranchUrl + "&start="+start + "&limit="+limit;
    	 console.log("url is: "+url);
    	 var t = navbuilder.project("PROJECT_1").allRepos()
         .withParams({
             start : start,
             limit : limit
         }).build();
    	 console.log("working url: "+t);
    	 return url; 
     };

     BranchTable.prototype.handleNewRows = function (data, attachmentMethod) {
         var self = this;
         console.log("Woo, trying to get more rows!!");
         var rows = _.map(data.values, function(repo) {
             repo.project = {
                 key : self.projectKey
             };
             return stash.feature.repository.repositoryRow(repo);
         });
         this.$table.show().children("tbody")[attachmentMethod !== 'html' ? attachmentMethod : 'append'](rows.join(''));
     };

     BranchTable.prototype.handleErrors = function (errors) {
     };

     return BranchTable;
 }

);
