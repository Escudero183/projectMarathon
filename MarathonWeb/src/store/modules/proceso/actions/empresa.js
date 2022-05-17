import service from '@/services/modules/proceso/empresa'

export default {
  EMPRESA_FIND_ALL(_, params) {
    return new Promise((resolve, reject) => {
      service.findAll(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
  EMPRESA_FIND_BY_ID(_, params) {
    return new Promise((resolve, reject) => {
      service.findById(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
  EMPRESA_CREATE(_, params) {
    return new Promise((resolve, reject) => {
      service.create(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
  EMPRESA_UPDATE(_, params) {
    return new Promise((resolve, reject) => {
      service.update(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
  EMPRESA_DELETE(_, params) {
    return new Promise((resolve, reject) => {
      service.delete(params).then(res => {
        resolve(res)
      }).catch(err => {
        reject(err)
      })
    })
  },
}
