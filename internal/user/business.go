package user

type Business struct {
    repository *Repository
}

func NewBusiness(repository *Repository) *Business {
    return &Business{repository: repository}
}
