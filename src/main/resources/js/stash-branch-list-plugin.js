define('page/branch/branch-list', [
    'exports',
    'jquery',
    'underscore',
    'aui',
    'model/page-state',
    'util/ajax',
    'feature/repository/branch-selector',
    'backbone-brace',
    'widget/paged-table',
    'util/events'
], function(
    exports,
    $,
    _,
    AJS,
    pageState,
    ajax,
    BranchSelector,
    Brace,
    PagedTable,
    events
) {
	
	var Branch = Brace.Model.extend( {
		namedAttributes : {
            'id' : null,
            'displayId' : null,
            'isDefault' : null,
            'message' : null,
            'latestChangeset' : null
        }
	});
	
	var BranchStatus = Brace.Model.extend({
		namedAttributes: {
			'branch': Branch,
			'aheadCount': 'number',
			'behindCount': 'number',
			'behindPercentage': 'number',
			'aheadPercentage': 'number'
		}
	});
	
	var BranchStatusCollection = Brace.Collection.extend({
		model: BranchStatus
	});
	
	function BranchListTableView(el, model) {
        this.model = model;
        
        this.model.on('refresh', _.bind(this.refreshTable, this));
	}
	
	BranchListTableView.prototype.refreshTable = function(branchList, collection, options) {	
		var $tableData;
        if (branchList.values.length == 0) {
            // no branches currently exist, don't do anything
        } else {
        	console.log("trying to re-render branch comparisons: "+branchList);
        	//this.$el = $(stash.page.branchPermissions.restrictedRefTable({ values : [restrictedRef.toJSON()] })).replaceAll(this.$el);
            $tableData = stash.plugin.branchlist.branchListTable({values: branchList.values} );
            console.log("tableData is "+$tableData);
        }
        $('.branch-list-table').replaceWith($tableData);
	};
	
	function resourceUrl(branchId) {
		return AJS.contextPath() + '/rest/branchcomparison/1.0/projects/'+ pageState.getProject().getKey() + '/repos/' + pageState.getRepository().getSlug() +'?branch='+ branchId;
	}
	
	
	function onReady(tableJSON, avatarSize, currentBranchId) {	
		console.log(tableJSON.values);
		var branchList = new BranchStatusCollection(tableJSON.values, {avatarSize: avatarSize});
		var view = new BranchListTableView($('.branch-list-table'), branchList);
		
		events.on('stash.feature.repository.revisionReferenceSelector.revisionRefChanged', function(revisionRef) {
		    var branchSelectorField = this;
		    var branchId = revisionRef.getId();
		    console.log("current branch id is: "+currentBranchId);
		    console.log("selected branch id is: "+branchId);
		    if (branchId != currentBranchId) {
		    	console.log("branch has changed");
		    	currentBranchId = branchId;
		    	$.ajax({ 
		    		type: "GET",
		    		dataType: "json",
		    		url: resourceUrl(branchId),
		    		success: function(data){   
		    			console.log(data);
		    			view.refreshTable(data);
		    		}
		    	});
		    } else {
		    	console.log("branch has not changed from the initial default");
		    }
		});
	}

    exports.onReady = function(tableJSON, avatarSize, initialBranchId) {
    	console.log("in here yo: "+initialBranchId);
        onReady(tableJSON, avatarSize, initialBranchId);
    };

});
