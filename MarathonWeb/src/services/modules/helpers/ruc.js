import apiCall from '../../index'

const apiService = '/api-marathon/api/v1/helper/ruc/'
const apiService2 = '/api-marathon/api/v1/helper/ruc2/'
const authorization = 'Authorization'
const tipoToken = 'Bearer'

const service = {
  getDatosByRuc(params) {
    return new Promise((resolve, reject) => {
      apiCall
        .defaults
        .headers
        .common[authorization] = `${tipoToken} ${localStorage.getItem('accessToken')}`
      apiCall
        .get(apiService + params.dni)
        .then(res => resolve(res.data))
        .catch(err => reject(err))
    })
  },

  getDatosByRuc2(params) {
    return new Promise((resolve, reject) => {
      apiCall
        .defaults
        .headers
        .common[authorization] = `${tipoToken} ${localStorage.getItem('accessToken')}`
      apiCall
        .get(`${apiService2}${params.ruc}?tipo=${params.tipo}`)
        .then(res => resolve(res.data))
        .catch(err => reject(err))
    })
  },
}

export default service
