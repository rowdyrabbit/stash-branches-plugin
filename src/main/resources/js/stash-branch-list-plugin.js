define('page/branch/branch-list', [
    'exports',
    'jquery',
    'underscore',
    'aui',
    'model/page-state',
    'util/ajax',
    'backbone-brace',
    'util/events',
    'util/navbuilder'
], function(
    exports,
    $,
    _,
    AJS,
    pageState,
    ajax,
    Brace,
    events,
    navBuilder
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
			'aheadPercentage': 'number',
			'branchUrl': 'string'
		}
	});
	
	var BranchStatusCollection = Brace.Collection.extend({
		model: BranchStatus
	});
	
	function BranchListTableView(el, model) {
        this.model = model;
	}
	
	BranchListTableView.prototype.refreshTable = function(branchList) {
		var $tableData;
        if (branchList.values.length == 0) {
            // no branches currently exist, don't do anything
        } else {
            $tableData = stash.plugin.branchlist.branchListTable({values: branchList.values, comparisonBranch : branchList.comparisonBranch} );
        }
        $('.branch-list-table').replaceWith($tableData);
	};
	
	function resourceUrl(branchId) {
		return AJS.contextPath() + '/rest/branchcomparison/1.0/projects/'+ pageState.getProject().getKey() + '/repos/' + pageState.getRepository().getSlug() +'?branch='+ branchId;
	}
	
	function buildUrl(projectKey, repoSlug, path, revisionRef) {
        var nav = navBuilder.project(projectKey).repo(repoSlug).browse().path(path);
        return nav.at(revisionRef).build();
    }
	
	function onReady(tableJSON, avatarSize, currentBranchId) {	
		this.currentBranchId = currentBranchId;
		var branchList = new BranchStatusCollection(tableJSON.values, {avatarSize: avatarSize});
		var view = new BranchListTableView($('.branch-list-table'), branchList);
		
		events.on('stash.feature.repository.revisionReferenceSelector.revisionRefChanged', function(revisionRef) {
		    var branchId = revisionRef.getId();
		    if (branchId != currentBranchId) {
		    	currentBranchId = branchId;
		    	$.ajax({ 
		    		type: "GET",
		    		dataType: "json",
		    		url: resourceUrl(branchId),
		    		success: function(data){   
		    			view.refreshTable(data);
		    		}
		    	});
		    } 
		});
	}

    exports.onReady = function(tableJSON, avatarSize, initialBranchId) {
        onReady(tableJSON, avatarSize, initialBranchId);
    };

});
