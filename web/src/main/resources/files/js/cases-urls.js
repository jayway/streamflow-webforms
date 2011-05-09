/*
 *
 * Copyright 2009-2010 Streamsource AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var UrlModule = (function () {
    var inner = {};
    var enduserid;
//    var casesDataSource;
   
    var urls = {
    	proxy: 		"proxy/",
        mypages:    "surface/",
    };

//    var openCasesQuery = "select label caseid �rendeId,description,created,project,status,href";
    var openCasesQuery = "select href,caseid,description,created,project,status,lastupdated,lastmessage order by lastupdated desc";
    var closedCasesQuery = "select href,caseid,description,created,project,closed,resolution order by closed desc";
    var caseHistoryQuery = "select message,created order by created desc";
    var casesTotalQuery = "select description";
    var casesQuery = {};

    inner.setUserId = function (userid) {
    	this.enduserid = userid;
    	urls.endusers = '/' + urls.mypages + urls.proxy + 'endusers/';
    	urls.enduser = urls.endusers + this.enduserid + '/';
    };
    
    inner.verifyEndUser = function () {
    	return urls.enduser;
    };
    
    inner.createUser = function () {
    	return urls.endusers + 'create?string=' + this.enduserid;
    };
    
    inner.setupOpenCasesQuery = function () {
    	casesQuery = openCasesQuery;
    };
    
    inner.setupClosedCasesQuery = function () {
    	casesQuery = closedCasesQuery;
    };
    
    inner.setupOpenCasesDataSource = function () {
    	this.casesDataSource = urls.enduser + 'open/cases.json';
    	this.caseDataSource = urls.enduser + 'open/';
    };
    
    inner.setupClosedCasesDataSource = function () {
    	this.casesDataSource = urls.enduser + 'closed/cases.json';
    	this.caseDataSource = urls.enduser + 'closed/';
    };
    
    inner.getCasesDataSource = function () {
    	return this.casesDataSource;
    };
    
    function getOpenCasesDataSource() {
		return urls.enduser + 'open/cases.json';
	};
	
	function getClosedCasesDataSource() {
		return urls.enduser + 'closed/cases.json';
	};
	
	inner.getOpenCasesTotal = function () {
		return this.casesDataSource + '?tq=' + casesTotalQuery;
	};

	inner.getClosedCasesTotal = function () {
		return this.casesDataSource + '?tq=' + casesTotalQuery;
	};

    inner.getCaseDataSource = function () {
    	return this.caseDataSource;
    };
    
    inner.getCasesQuery = function () {
    	return casesQuery;
    };
    
    inner.getOpenCases = function () {
    	return urls.enduser + 'open/cases.json' + '?tq=' + casesQuery;
    };
    
    inner.getCase = function (entityid) {
    	return this.caseDataSource + entityid;
    };
    
    inner.getCaseHistoryQuery = function (entityid) {
    	return caseHistoryQuery;
    };
    
    inner.getCaseHistoryDataSource = function (entityid) {
    	return this.caseDataSource + entityid + 'history';
    };
    
    inner.getCaseHistory = function (entityid) {
    	return this.caseDataSource + entityid + 'history?tq=' + caseHistoryQuery;
    };
    
    inner.getClosedCases = function () {
    	return urls.enduser + 'closed/cases.json' + '?tq=' + casesQuery;
    };
    
    return inner;
}());