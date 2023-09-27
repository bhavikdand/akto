import request from '@/util/request'

export default {
    fetchIssues(skip, limit, filterStatus, filterCollectionsId, filterSeverity, filterSubCategory, startEpoch) {
        return request({
            url: 'api/fetchAllIssues',
            method: 'post',
            data: {skip, limit, filterStatus, filterCollectionsId, filterSeverity, filterSubCategory, startEpoch}
        })
    },
    fetchVulnerableTestingRunResultsFromIssues(filters, skip) {
        filters['skip'] = skip
        return request({
            url: 'api/fetchVulnerableTestingRunResultsFromIssues',
            method: 'post',
            data: filters
        })
    },
    updateIssueStatus(issueId, statusToBeUpdated, ignoreReason) {
        return request({
            url: 'api/updateIssueStatus',
            method: 'post',
            data: {issueId, statusToBeUpdated, ignoreReason}
        })
    },
    bulkUpdateIssueStatus (issueIdArray, statusToBeUpdated, ignoreReason) {
        return request({
            url: 'api/bulkUpdateIssueStatus',
            method: 'post',
            data: {issueIdArray, statusToBeUpdated, ignoreReason}
        })
    },
    fetchTestingRunResult (issueId) {
        return request({
            url: 'api/fetchTestingRunResult',
            method: 'post',
            data: {issueId}
        })
    },
    fetchAffectedEndpoints (issueId) {
        return request({
            url: 'api/fetchAffectedEndpoints',
            method: 'post',
            data: {issueId}
        })
    },
    fetchAllSubCategories () {
        return request({
            url: 'api/fetchAllSubCategories',
            method: 'post',
            data: {}
        })
    }
}