import service from '@/services/modules/helpers/ruc'

export default {
  GET_DATOS_BY_RUC(_, params) {
    return new Promise((resolve, reject) => {
      service.getDatosByRuc(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
  GET_DATOS_BY_RUC2(_, params) {
    return new Promise((resolve, reject) => {
      service.getDatosByRuc2(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
}
